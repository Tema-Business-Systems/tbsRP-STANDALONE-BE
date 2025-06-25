package com.transport.hos.service;

import com.transport.hos.model.DriverLog;
import com.transport.hos.repository.DriverLogsRepository;
import com.transport.hos.response.DriverLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DriverLogService {

    @Autowired
    private DriverLogsRepository driverLogRepository;

    // Convert VO to Entity
    private DriverLog convertToEntity(DriverLogVO vo) {
        DriverLog entity = new DriverLog();
        entity.setUpdateTicket(vo.getUpdateTicket());
        entity.setLogId(vo.getLogId());
        entity.setDriverId(vo.getDriverId());
        entity.setVehicleId(vo.getVehicleId());
        entity.setStartDate(vo.getStartDate());
        entity.setStartTime(vo.getStartTime());
        entity.setEndDate(vo.getEndDate());
        entity.setEndTime(vo.getEndTime());
        entity.setTotalHours(vo.getTotalHours());
        entity.setStartLatitude(vo.getStartLat());
        entity.setStartLongitude(vo.getStartLng());
        entity.setEndLatitude(vo.getEndLat());
        entity.setEndLongitude(vo.getEndLng());
        entity.setLogType(vo.getLogType());
        entity.setCreatedDateTime(vo.getCreatedDateTime());
        entity.setUpdatedDateTime(vo.getUpdatedDateTime());
        entity.setAuuid(vo.getAuuid());
        entity.setCreatedUser(vo.getCreatedUser());
        entity.setUpdatedUser(vo.getUpdatedUser());
        entity.setRowId(vo.getRowId());
        return entity;
    }

    // Convert Entity to VO
    private DriverLogVO convertToVO(DriverLog entity) {
        DriverLogVO vo = new DriverLogVO();
        vo.setUpdateTicket(entity.getUpdateTicket());
        vo.setLogId(entity.getLogId());
        vo.setDriverId(entity.getDriverId());
        vo.setVehicleId(entity.getVehicleId());
        vo.setStartDate(entity.getStartDate());
        vo.setStartTime(entity.getStartTime());
        vo.setEndDate(entity.getEndDate());
        vo.setEndTime(entity.getEndTime());
        vo.setTotalHours(entity.getTotalHours());
        vo.setStartLat(entity.getStartLatitude());
        vo.setStartLng(entity.getStartLongitude());
        vo.setEndLat(entity.getEndLatitude());
        vo.setEndLng(entity.getEndLongitude());
        vo.setLogType(entity.getLogType());
        vo.setCreatedDateTime(entity.getCreatedDateTime());
        vo.setUpdatedDateTime(entity.getUpdatedDateTime());
        vo.setAuuid(entity.getAuuid());
        vo.setCreatedUser(entity.getCreatedUser());
        vo.setUpdatedUser(entity.getUpdatedUser());
        vo.setRowId(entity.getRowId());
        return vo;
    }

    // Create or Update
    public DriverLogVO saveLog(DriverLogVO vo) {
        DriverLog entity = convertToEntity(vo);
        entity = driverLogRepository.save(entity);
        return convertToVO(entity);
    }

    // Get by ID
    public DriverLogVO getLogById(int logId) {
        return driverLogRepository.findById(logId)
                .map(this::convertToVO)
                .orElse(null);
    }

    // Get All
    public List<DriverLogVO> getAllLogs() {
        return StreamSupport.stream(driverLogRepository.findAll().spliterator(), false)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    // Delete by ID
    public void deleteLog(int logId) {
        driverLogRepository.deleteById(logId);
    }

    public DriverLogVO updateLog(int logId, DriverLogVO vo) {
        return driverLogRepository.findById(logId).map(existingLog -> {
            existingLog.setUpdateTicket(vo.getUpdateTicket());
            existingLog.setDriverId(vo.getDriverId());
            existingLog.setVehicleId(vo.getVehicleId());
            existingLog.setStartDate(vo.getStartDate());
            existingLog.setStartTime(vo.getStartTime());
            existingLog.setEndDate(vo.getEndDate());
            existingLog.setEndTime(vo.getEndTime());
            existingLog.setTotalHours(vo.getTotalHours());
            existingLog.setStartLatitude(vo.getStartLat());
            existingLog.setStartLongitude(vo.getStartLng());
            existingLog.setEndLatitude(vo.getEndLat());
            existingLog.setEndLongitude(vo.getEndLng());
            existingLog.setLogType(vo.getLogType());
            existingLog.setUpdatedDateTime(vo.getUpdatedDateTime());
            existingLog.setAuuid(vo.getAuuid());
            existingLog.setUpdatedUser(vo.getUpdatedUser());

            return convertToVO(driverLogRepository.save(existingLog));
        }).orElse(null);
    }

}

