package com.dispatchoptimizer.dispatchbalancer.mapper;

import com.dispatchoptimizer.dispatchbalancer.dto.OrderDto;
import com.dispatchoptimizer.dispatchbalancer.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toEntity(OrderDto dto) {
        return Order.builder()
                .orderId(dto.getOrderId())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .address(dto.getAddress())
                .packageWeight(dto.getPackageWeight())
                .priority(dto.getPriority())
                .assignedVehicleId("unassigned")
                .build();
    }

    public OrderDto toDto(Order entity) {
        return OrderDto.builder()
                .orderId(entity.getOrderId())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .address(entity.getAddress())
                .packageWeight(entity.getPackageWeight())
                .priority(entity.getPriority())
                .build();
    }
}
