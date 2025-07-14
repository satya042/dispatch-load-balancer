package com.dispatchoptimizer.dispatchbalancer.service.impl;

import com.dispatchoptimizer.dispatchbalancer.dto.OrderDto;
import com.dispatchoptimizer.dispatchbalancer.dto.VehicleDispatchDto;
import com.dispatchoptimizer.dispatchbalancer.dto.VehicleDto;
import com.dispatchoptimizer.dispatchbalancer.entity.Order;
import com.dispatchoptimizer.dispatchbalancer.entity.Vehicle;
import com.dispatchoptimizer.dispatchbalancer.exception.DuplicateResourceException;
import com.dispatchoptimizer.dispatchbalancer.mapper.OrderMapper;
import com.dispatchoptimizer.dispatchbalancer.mapper.VehicleMapper;
import com.dispatchoptimizer.dispatchbalancer.response.DispatchResponse;
import com.dispatchoptimizer.dispatchbalancer.service.DispatchService;
import com.dispatchoptimizer.dispatchbalancer.util.HaversineUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dispatchoptimizer.dispatchbalancer.repository.OrderRepository;
import com.dispatchoptimizer.dispatchbalancer.repository.VehicleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService {
    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final OrderMapper orderMapper;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public void saveOrders(List<OrderDto> orders) {
        List<Order> orderList = orders.stream()
                .map(orderMapper::toEntity)
                .collect(Collectors.toList());
        orderRepository.saveAll(orderList);
    }

    @Override
    @Transactional
    public void saveVehicles(List<VehicleDto> vehicles) {
//        List<Vehicle> vehicleList = vehicles.stream()
//                .map(vehicleMapper::toEntity)
//                .collect(Collectors.toList());

        List<String> vehicleIds = vehicles.stream()
                .map(VehicleDto::getVehicleId)
                .toList();

        List<Vehicle> existing = vehicleRepository.findAllById(vehicleIds);

        if (!existing.isEmpty()) {
            String ids = existing.stream()
                    .map(Vehicle::getVehicleId)
                    .collect(Collectors.joining(", "));
            throw new DuplicateResourceException("Vehicle(s) with ID(s) already exist: " + ids);
        }

        List<Vehicle> vehicleList = vehicles.stream()
                .map(vehicleMapper::toEntity)
                .collect(Collectors.toList());
        vehicleRepository.saveAll(vehicleList);
    }

    @Override
    @Transactional
    public DispatchResponse generateDispatchPlan() {
        List<Order> unassignedOrders = orderRepository.findByAssignedVehicleId("unassigned");
        List<Vehicle> vehicles = vehicleRepository.findAll();

        Map<String, Double> remainingCapacities = initializeCapacities(vehicles);
        Map<String, List<Order>> vehicleToOrders = new HashMap<>();
        Map<String, Double> vehicleToDistance = new HashMap<>();
        List<Order> updatedOrders = new ArrayList<>();

        unassignedOrders.sort(Comparator.comparingInt(o -> o.getPriority().getRank()));

        for (Order order : unassignedOrders) {
            Vehicle bestVehicle = findBestVehicle(order, vehicles, remainingCapacities);
            if (bestVehicle != null) {
                assignOrderToVehicle(order, bestVehicle, remainingCapacities, vehicleToOrders, vehicleToDistance);
                updatedOrders.add(order);
            }
        }
        orderRepository.saveAll(updatedOrders);

        List<VehicleDispatchDto> dispatchVehicles = buildDispatchResponse(vehicles, vehicleToOrders, vehicleToDistance);
        return DispatchResponse.builder().dispatchPlan(dispatchVehicles).build();
    }

    private Map<String, Double> initializeCapacities(List<Vehicle> vehicles) {
        return vehicles.stream().collect(Collectors.toMap(Vehicle::getVehicleId, Vehicle::getCapacity));
    }

    private Vehicle findBestVehicle(Order order, List<Vehicle> vehicles, Map<String, Double> remainingCapacities) {
        Vehicle bestVehicle = null;
        double minDistance = Double.MAX_VALUE;

        for (Vehicle vehicle : vehicles) {
            double remaining = remainingCapacities.get(vehicle.getVehicleId());
            if (remaining < order.getPackageWeight()) continue;

            double distance = HaversineUtil.calculateDistance(
                    vehicle.getCurrentLatitude(), vehicle.getCurrentLongitude(),
                    order.getLatitude(), order.getLongitude());

            if (distance < minDistance) {
                minDistance = distance;
                bestVehicle = vehicle;
            }
        }
        return bestVehicle;
    }

    private void assignOrderToVehicle(Order order, Vehicle vehicle, Map<String, Double> capacityMap,
                                      Map<String, List<Order>> vehicleToOrders, Map<String, Double> vehicleToDistance) {
        String vehicleId = vehicle.getVehicleId();
        capacityMap.put(vehicleId, capacityMap.get(vehicleId) - order.getPackageWeight());
        order.setAssignedVehicleId(vehicleId);

        double distance = HaversineUtil.calculateDistance(
                vehicle.getCurrentLatitude(), vehicle.getCurrentLongitude(),
                order.getLatitude(), order.getLongitude());

        vehicleToOrders.computeIfAbsent(vehicleId, k -> new ArrayList<>()).add(order);
        vehicleToDistance.put(vehicleId, vehicleToDistance.getOrDefault(vehicleId, 0.0) + distance);
    }

    private List<VehicleDispatchDto> buildDispatchResponse(List<Vehicle> vehicles,
                                                           Map<String, List<Order>> vehicleToOrders,
                                                           Map<String, Double> vehicleToDistance) {
        List<VehicleDispatchDto> result = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            String vid = vehicle.getVehicleId();
            List<Order> assigned = vehicleToOrders.getOrDefault(vid, List.of());

            List<OrderDto> orderDtos = assigned.stream()
                    .map(orderMapper::toDto)
                    .collect(Collectors.toList());

            double totalLoad = assigned.stream().mapToDouble(Order::getPackageWeight).sum();
            double totalDistance = vehicleToDistance.getOrDefault(vid, 0.0);

            result.add(VehicleDispatchDto.builder()
                    .vehicleId(vid)
                    .totalLoad(totalLoad)
                    .totalDistance(String.format("%.1f km", totalDistance))
                    .assignedOrders(orderDtos)
                    .build());
        }
        return result;
    }
}
