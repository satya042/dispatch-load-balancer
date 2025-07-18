package com.dispatchoptimizer.dispatchbalancer.repository;

import com.dispatchoptimizer.dispatchbalancer.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByAssignedVehicleId(String assignedVehicleId);
}
