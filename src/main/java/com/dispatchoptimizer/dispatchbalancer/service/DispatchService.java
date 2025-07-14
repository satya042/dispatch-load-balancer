package com.dispatchoptimizer.dispatchbalancer.service;

import com.dispatchoptimizer.dispatchbalancer.dto.OrderDto;
import com.dispatchoptimizer.dispatchbalancer.dto.VehicleDto;
import com.dispatchoptimizer.dispatchbalancer.response.DispatchResponse;

import java.util.List;

public interface DispatchService {
    void saveOrders(List<OrderDto> orders);
    void saveVehicles(List<VehicleDto> vehicles);
    DispatchResponse generateDispatchPlan();
}
