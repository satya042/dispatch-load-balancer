package com.dispatchoptimizer.dispatchbalancer.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleDto {

    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;

    @Positive(message = "Capacity must be positive")
    private double capacity;

    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90.0")
    private double currentLatitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180.0")
    private double currentLongitude;

    @NotBlank(message = "Address is required")
    private String currentAddress;
}
