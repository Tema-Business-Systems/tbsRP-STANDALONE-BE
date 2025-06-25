package com.transport.fleet.repository;

import com.transport.fleet.model.FleetDriver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FleetDriverRepository extends CrudRepository<FleetDriver, Long> {
    FleetDriver findByDriverId(String driverId);
    List<FleetDriver> findAll();
    void deleteByDriverId(String driverId);
    boolean existsByDriverId(String driverId);
}
