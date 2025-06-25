package com.transport.hos.repository;

import com.transport.hos.model.DriverBreak;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Repository for DriverBreaks
public interface DriverBreakRepository extends CrudRepository<DriverBreak, Integer> {
    List<DriverBreak> findByDriverId(String driverId);
    List<DriverBreak> findByTripId(String tripId);
}

