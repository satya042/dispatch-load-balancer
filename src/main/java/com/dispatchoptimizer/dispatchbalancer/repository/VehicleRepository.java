package com.dispatchoptimizer.dispatchbalancer.repository;

import com.dispatchoptimizer.dispatchbalancer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
