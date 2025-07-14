package com.dispatchoptimizer.dispatchbalancer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDispatchDto {
    private String vehicleId;
    private double totalLoad;
    private String totalDistance;
    private List<OrderDto> assignedOrders;
}
