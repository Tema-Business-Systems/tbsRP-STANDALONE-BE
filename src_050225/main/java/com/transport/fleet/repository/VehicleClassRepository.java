package com.transport.fleet.repository;

import com.transport.fleet.model.VehClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleClassRepository extends JpaRepository<VehClass, Long> {
    List<VehClass> findAll();
    VehClass findByClassName(String className);
    void deleteByClassName(String className);
    boolean existsByClassName(String className);
}
