package com.transport.tracking.repository;

import com.transport.tracking.model.Docs;
import com.transport.tracking.model.Driver;
import com.transport.tracking.model.DriverSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface DriverProRepository extends CrudRepository<DriverSchedule, BigDecimal> {

    public DriverSchedule findByDriverid(String driverId);


    public List<DriverSchedule> findAll();

    @Query(value="select * from TMSNEW.VW_DRIVER_HOS_RAW c where c.DRIVERID_0 IN (?1) and (c.ACTIVITY_DATE between ?2 AND ?3) ORDER BY c.ACTIVITY_DATE DESC",nativeQuery = true)
    public List<DriverSchedule> findByDriverLogswithDateRange(String driverid, Date sdate, Date edate);


}
