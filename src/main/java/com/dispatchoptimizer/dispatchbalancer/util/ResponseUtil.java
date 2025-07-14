package com.dispatchoptimizer.dispatchbalancer.util;

import com.dispatchoptimizer.dispatchbalancer.response.ApiResponse;

public class ResponseUtil {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("error", message, data);
    }
}
