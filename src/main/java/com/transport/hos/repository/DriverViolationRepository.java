package com.transport.hos.repository;

import com.transport.hos.model.DriverViolation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Repository for DriverViolation
public interface DriverViolationRepository extends CrudRepository<DriverViolation, Integer> {
    List<DriverViolation> findByTripId(String tripId);
    List<DriverViolation> findByViolationType(byte violationType);
}

