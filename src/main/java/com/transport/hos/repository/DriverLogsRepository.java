package com.transport.hos.repository;

import com.transport.hos.model.DriverLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Repository for DriverLogs
public interface DriverLogsRepository extends CrudRepository<DriverLog, Integer> {
    List<DriverLog> findByDriverId(String driverId);
    List<DriverLog> findByVehicleId(String vehicleId);
}

