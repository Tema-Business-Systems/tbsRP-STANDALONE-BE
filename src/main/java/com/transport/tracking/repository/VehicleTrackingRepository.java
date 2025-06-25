package com.transport.tracking.repository;

import com.transport.tracking.model.DocReportTrack;
import com.transport.tracking.model.VehLiveTrack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Repository
public interface VehicleTrackingRepository extends CrudRepository<VehLiveTrack, Integer> {

    List<VehLiveTrack> findBySite(String site);

    List<VehLiveTrack> findBySiteAndCurrDate(String site, String date);

    public List<VehLiveTrack> findAll();

    public VehLiveTrack findByVehicle(String vehicle);

    @Query(value="select * from TMSNEW.XTMSVEHTRACKING c where c.SITE = ?1 and (c.DATE between ?2 AND ?3)",nativeQuery = true)
    public List<VehLiveTrack> getVehBySiteAndDateRange(String site, String sdate, String edate);


}
