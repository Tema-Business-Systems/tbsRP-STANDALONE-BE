package com.transport.hos.service;

import com.transport.hos.model.DriverBreak;
import com.transport.hos.repository.DriverBreakRepository;
import com.transport.hos.response.DriverBreakVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DriverBreakService {

    @Autowired
    private DriverBreakRepository driverBreakRepository;

    // Convert VO to Entity
    private DriverBreak convertToEntity(DriverBreakVO vo) {
        DriverBreak entity = new DriverBreak();
        entity.setUpdateTicket(vo.getUpdateTicket());
        entity.setBreakId(vo.getBreakId());
        entity.setDriverId(vo.getDriverId());
        entity.setTripId(vo.getTripId());
        entity.setStartDate(vo.getStartDate());
        entity.setStartTime(vo.getStartTime());
        entity.setEndDate(vo.getEndDate());
        entity.setEndTime(vo.getEndTime());
        entity.setTotalHours(vo.getTotalHours());
        entity.setStartLatitude(vo.getStartLat());
        entity.setStartLongitude(vo.getStartLng());
        entity.setEndLatitude(vo.getEndLat());
        entity.setEndLongitude(vo.getEndLng());
        entity.setBreakType(vo.getBreakType());
        entity.setCreatedDateTime(vo.getCreatedDateTime());
        entity.setUpdatedDateTime(vo.getUpdatedDateTime());
        entity.setAuuid(vo.getAuuid());
        entity.setCreatedUser(vo.getCreatedUser());
        entity.setUpdatedUser(vo.getUpdatedUser());
        return entity;
    }

    // Convert Entity to VO
    private DriverBreakVO convertToVO(DriverBreak entity) {
        DriverBreakVO vo = new DriverBreakVO();
        vo.setUpdateTicket(entity.getUpdateTicket());
        vo.setBreakId(entity.getBreakId());
        vo.setDriverId(entity.getDriverId());
        vo.setTripId(entity.getTripId());
        vo.setStartDate(entity.getStartDate());
        vo.setStartTime(entity.getStartTime());
        vo.setEndDate(entity.getEndDate());
        vo.setEndTime(entity.getEndTime());
        vo.setTotalHours(entity.getTotalHours());
        vo.setStartLat(entity.getStartLatitude());
        vo.setStartLng(entity.getStartLongitude());
        vo.setEndLat(entity.getEndLatitude());
        vo.setEndLng(entity.getEndLongitude());
        vo.setBreakType(entity.getBreakType());
        vo.setCreatedDateTime(entity.getCreatedDateTime());
        vo.setUpdatedDateTime(entity.getUpdatedDateTime());
        vo.setAuuid(entity.getAuuid());
        vo.setCreatedUser(entity.getCreatedUser());
        vo.setUpdatedUser(entity.getUpdatedUser());
        return vo;
    }

    // Create or Update
    public DriverBreakVO saveBreak(DriverBreakVO vo) {
        DriverBreak entity = convertToEntity(vo);
        entity = driverBreakRepository.save(entity);
        return convertToVO(entity);
    }

    // Get by ID
    public DriverBreakVO getBreakById(int breakId) {
        return driverBreakRepository.findById(breakId)
                .map(this::convertToVO)
                .orElse(null);
    }

    // Get All
    public List<DriverBreakVO> getAllBreaks() {
        return StreamSupport.stream(driverBreakRepository.findAll().spliterator(), false)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    // Delete by ID
    public void deleteBreak(int breakId) {
        driverBreakRepository.deleteById(breakId);
    }

    public DriverBreakVO updateBreak(int breakId, DriverBreakVO vo) {
        return driverBreakRepository.findById(breakId).map(existingBreak -> {
            existingBreak.setUpdateTicket(vo.getUpdateTicket());
            existingBreak.setDriverId(vo.getDriverId());
            existingBreak.setTripId(vo.getTripId());
            existingBreak.setStartDate(vo.getStartDate());
            existingBreak.setStartTime(vo.getStartTime());
            existingBreak.setEndDate(vo.getEndDate());
            existingBreak.setEndTime(vo.getEndTime());
            existingBreak.setTotalHours(vo.getTotalHours());
            existingBreak.setStartLatitude(vo.getStartLat());
            existingBreak.setStartLongitude(vo.getStartLng());
            existingBreak.setEndLatitude(vo.getEndLat());
            existingBreak.setEndLongitude(vo.getEndLng());
            existingBreak.setBreakType(vo.getBreakType());
            existingBreak.setUpdatedDateTime(vo.getUpdatedDateTime());
            existingBreak.setAuuid(vo.getAuuid());
            existingBreak.setUpdatedUser(vo.getUpdatedUser());

            return convertToVO(driverBreakRepository.save(existingBreak));
        }).orElse(null);
    }

}

