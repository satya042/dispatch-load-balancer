package com.dispatchoptimizer.dispatchbalancer.dto;

import com.dispatchoptimizer.dispatchbalancer.enums.Priority;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90.0")
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180.0")
    private double longitude;

    @NotBlank(message = "Address is required")
    private String address;

    @DecimalMin(value = "0.1", inclusive = true, message = "Package weight must be positive")
    private double packageWeight;

    @NotNull(message = "Priority is required")
    private Priority priority;
}
