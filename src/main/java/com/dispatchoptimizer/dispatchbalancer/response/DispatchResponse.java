package com.dispatchoptimizer.dispatchbalancer.response;

import com.dispatchoptimizer.dispatchbalancer.dto.VehicleDispatchDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DispatchResponse {
    private List<VehicleDispatchDto> dispatchPlan;
}
