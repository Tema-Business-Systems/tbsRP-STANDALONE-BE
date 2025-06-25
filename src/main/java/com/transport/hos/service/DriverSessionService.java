package com.transport.hos.service;

import com.transport.hos.model.DriverSession;
import com.transport.hos.repository.DriverSessionRepository;
import com.transport.hos.response.DriverSessionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverSessionService {

    @Autowired
    private DriverSessionRepository driverSessionRepository;

    // **ADD (CREATE) Method**
    public DriverSessionVO saveSession(DriverSessionVO vo) {
        DriverSession entity = convertToEntity(vo);
        return convertToVO(driverSessionRepository.save(entity));
    }

    // **GET (READ) Method**
    public DriverSessionVO getSessionById(int sessionId) {
        Optional<DriverSession> entity = driverSessionRepository.findById(sessionId);
        return entity.map(this::convertToVO).orElse(null);
    }

    public List<DriverSessionVO> getAllSessions() {
        List<DriverSession> sessions = (List<DriverSession>) driverSessionRepository.findAll();
        return sessions.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    // **UPDATE Method**
    public DriverSessionVO updateSession(int sessionId, DriverSessionVO vo) {
        return driverSessionRepository.findById(sessionId).map(existingSession -> {
            existingSession.setUpdateTicket(vo.getUpdateTicket());
            existingSession.setDriverId(vo.getDriverId());
            existingSession.setVehicleId(vo.getVehicleId());
            existingSession.setStartDate(vo.getStartDate());
            existingSession.setStartTime(vo.getStartTime());
            existingSession.setEndDate(vo.getEndDate());
            existingSession.setEndTime(vo.getEndTime());
            existingSession.setTotalHours(vo.getTotalHours());
            existingSession.setStartLatitude(vo.getStartLat());
            existingSession.setStartLongitude(vo.getStartLng());
            existingSession.setEndLatitude(vo.getEndLat());
            existingSession.setEndLongitude(vo.getEndLng());
            existingSession.setSessionType(vo.getSessionType());
            existingSession.setUpdatedDateTime(vo.getUpdatedDateTime());
            existingSession.setAuuid(vo.getAuuid());
            existingSession.setUpdatedUser(vo.getUpdatedUser());

            return convertToVO(driverSessionRepository.save(existingSession));
        }).orElse(null);
    }

    // **DELETE Method**
    public void deleteSession(int sessionId) {
        driverSessionRepository.deleteById(sessionId);
    }

    // **Helper Methods**
    private DriverSession convertToEntity(DriverSessionVO vo) {
        DriverSession entity = new DriverSession();
        entity.setUpdateTicket(vo.getUpdateTicket());
        entity.setSessionId(vo.getSessionId());
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
        entity.setSessionType(vo.getSessionType());
        entity.setCreatedDateTime(vo.getCreatedDateTime());
        entity.setUpdatedDateTime(vo.getUpdatedDateTime());
        entity.setAuuid(vo.getAuuid());
        entity.setCreatedUser(vo.getCreatedUser());
        entity.setUpdatedUser(vo.getUpdatedUser());
        return entity;
    }

    private DriverSessionVO convertToVO(DriverSession entity) {
        DriverSessionVO vo = new DriverSessionVO();
        vo.setUpdateTicket(entity.getUpdateTicket());
        vo.setSessionId(entity.getSessionId());
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
        vo.setSessionType(entity.getSessionType());
        vo.setCreatedDateTime(entity.getCreatedDateTime());
        vo.setUpdatedDateTime(entity.getUpdatedDateTime());
        vo.setAuuid(entity.getAuuid());
        vo.setCreatedUser(entity.getCreatedUser());
        vo.setUpdatedUser(entity.getUpdatedUser());
        return vo;
    }
}


