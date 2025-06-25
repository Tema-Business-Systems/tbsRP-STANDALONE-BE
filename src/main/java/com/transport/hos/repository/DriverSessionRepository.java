package com.transport.hos.repository;

import com.transport.hos.model.DriverSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Repository for DriverSession
public interface DriverSessionRepository extends CrudRepository<DriverSession, Integer> {
    List<DriverSession> findByDriverId(String driverId);
    List<DriverSession> findByVehicleId(String vehicleId);
}
