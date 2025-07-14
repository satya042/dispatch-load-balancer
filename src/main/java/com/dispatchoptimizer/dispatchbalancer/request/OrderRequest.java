package com.dispatchoptimizer.dispatchbalancer.request;

import com.dispatchoptimizer.dispatchbalancer.dto.OrderDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotEmpty(message = "Orders list must not be empty")
    private List< @Valid OrderDto> orders;
}
