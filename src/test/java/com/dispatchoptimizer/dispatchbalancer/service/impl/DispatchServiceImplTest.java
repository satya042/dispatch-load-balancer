package com.dispatchoptimizer.dispatchbalancer.service.impl;


import com.dispatchoptimizer.dispatchbalancer.dto.OrderDto;
import com.dispatchoptimizer.dispatchbalancer.dto.VehicleDto;
import com.dispatchoptimizer.dispatchbalancer.entity.Order;
import com.dispatchoptimizer.dispatchbalancer.entity.Vehicle;
import com.dispatchoptimizer.dispatchbalancer.enums.Priority;
import com.dispatchoptimizer.dispatchbalancer.mapper.OrderMapper;
import com.dispatchoptimizer.dispatchbalancer.mapper.VehicleMapper;
import com.dispatchoptimizer.dispatchbalancer.repository.OrderRepository;
import com.dispatchoptimizer.dispatchbalancer.repository.VehicleRepository;
import com.dispatchoptimizer.dispatchbalancer.response.DispatchResponse;
import com.dispatchoptimizer.dispatchbalancer.service.DispatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DispatchServiceImplTest {
    @InjectMocks
    private DispatchServiceImpl dispatchService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private VehicleMapper vehicleMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrders_shouldMapAndSaveOrders() {
        List<OrderDto> orderDtos = List.of(OrderDto.builder()
                .orderId("ORD123")
                .latitude(10)
                .longitude(20)
                .address("Addr")
                .packageWeight(50)
                .priority(Priority.HIGH)
                .build());

        Order order = Order.builder().orderId("ORD123").priority(Priority.HIGH).build();

        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);

        dispatchService.saveOrders(orderDtos);

        verify(orderRepository, times(1)).saveAll(anyList());
    }

    @Test
    void saveVehicles_shouldMapAndSaveVehicles() {
        List<VehicleDto> vehicleDtos = List.of(VehicleDto.builder()
                .vehicleId("VEH1")
                .capacity(100)
                .currentLatitude(10)
                .currentLongitude(20)
                .currentAddress("Hub")
                .build());

        Vehicle vehicle = Vehicle.builder().vehicleId("VEH1").capacity(100).build();

        when(vehicleMapper.toEntity(any(VehicleDto.class))).thenReturn(vehicle);

        dispatchService.saveVehicles(vehicleDtos);

        verify(vehicleRepository, times(1)).saveAll(anyList());
    }

    @Test
    void generateDispatchPlan_shouldAssignOrdersToVehiclesBasedOnConstraints() {
        Order order1 = Order.builder()
                .orderId("O1")
                .latitude(10).longitude(20).packageWeight(50).priority(Priority.HIGH)
                .assignedVehicleId("unassigned")
                .build();

        Vehicle vehicle1 = Vehicle.builder()
                .vehicleId("V1")
                .capacity(100)
                .currentLatitude(10).currentLongitude(21)
                .build();

        when(orderRepository.findByAssignedVehicleId("unassigned")).thenReturn(List.of(order1));
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1));

        when(orderMapper.toDto(any(Order.class)))
                .thenReturn(OrderDto.builder().orderId("O1").priority(Priority.HIGH).build());

        DispatchResponse response = dispatchService.generateDispatchPlan();

        assertThat(response.getDispatchPlan()).hasSize(1);
        assertThat(response.getDispatchPlan().get(0).getAssignedOrders()).hasSize(1);
        assertThat(response.getDispatchPlan().get(0).getVehicleId()).isEqualTo("V1");

        verify(orderRepository).saveAll(anyList());
    }
}
