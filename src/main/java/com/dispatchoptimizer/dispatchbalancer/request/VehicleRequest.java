package com.dispatchoptimizer.dispatchbalancer.request;

import com.dispatchoptimizer.dispatchbalancer.dto.VehicleDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class VehicleRequest {
    @NotEmpty(message = "Vehicles list must not be empty")
    private List<@Valid VehicleDto> vehicles;
}
