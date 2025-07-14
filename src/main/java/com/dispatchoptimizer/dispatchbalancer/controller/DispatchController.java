package com.dispatchoptimizer.dispatchbalancer.controller;

import com.dispatchoptimizer.dispatchbalancer.request.OrderRequest;
import com.dispatchoptimizer.dispatchbalancer.request.VehicleRequest;
import com.dispatchoptimizer.dispatchbalancer.response.ApiResponse;
import com.dispatchoptimizer.dispatchbalancer.response.DispatchResponse;
import com.dispatchoptimizer.dispatchbalancer.service.DispatchService;
import com.dispatchoptimizer.dispatchbalancer.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispatch")
@RequiredArgsConstructor
@Validated
public class DispatchController {

    private final DispatchService dispatchService;

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<String>> saveOrders(@Valid @RequestBody OrderRequest request) {
        dispatchService.saveOrders(request.getOrders());
        return ResponseEntity.ok(ResponseUtil.success("Delivery orders accepted.", null));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse<String>> saveVehicles(@Valid @RequestBody VehicleRequest request) {
        dispatchService.saveVehicles(request.getVehicles());
        return ResponseEntity.ok(ResponseUtil.success("Vehicle details accepted.", null));
    }

    @GetMapping("/plan")
    public ResponseEntity<ApiResponse<DispatchResponse>> getDispatchPlan() {
        DispatchResponse plan = dispatchService.generateDispatchPlan();
        return ResponseEntity.ok(ResponseUtil.success("Dispatch plan generated.", plan));
    }
}
