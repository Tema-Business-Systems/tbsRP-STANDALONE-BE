package com.transport.hos.repository;

import com.transport.hos.model.DriverList;
import org.springframework.data.repository.CrudRepository;
import java.math.BigDecimal;
import java.util.List;

public interface DriverListRepository extends CrudRepository<DriverList, BigDecimal> {

    public DriverList findByDriverId(String driverId);

    public List<DriverList> findBySite(String site);


    public List<DriverList> findAll();

}
