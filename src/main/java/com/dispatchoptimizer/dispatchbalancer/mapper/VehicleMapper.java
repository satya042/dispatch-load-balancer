package com.dispatchoptimizer.dispatchbalancer.mapper;

import com.dispatchoptimizer.dispatchbalancer.dto.VehicleDto;
import com.dispatchoptimizer.dispatchbalancer.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    public Vehicle toEntity(VehicleDto dto){
        return Vehicle.builder()
                .vehicleId(dto.getVehicleId())
                .capacity(dto.getCapacity())
                .currentLatitude(dto.getCurrentLatitude())
                .currentLongitude(dto.getCurrentLongitude())
                .currentAddress(dto.getCurrentAddress())
                .build();
    }

    public VehicleDto toDto(Vehicle entity){
        return VehicleDto.builder()
                .vehicleId(entity.getVehicleId())
                .capacity(entity.getCapacity())
                .currentLatitude(entity.getCurrentLatitude())
                .currentLongitude(entity.getCurrentLongitude())
                .currentAddress(entity.getCurrentAddress())
                .build();
    }
}
