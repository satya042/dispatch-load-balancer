package com.dispatchoptimizer.dispatchbalancer.entity;

import com.dispatchoptimizer.dispatchbalancer.enums.Priority;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;
    private double latitude;
    private double longitude;
    private String address;
    private double packageWeight;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private String assignedVehicleId;
}
