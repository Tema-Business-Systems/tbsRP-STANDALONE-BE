package com.transport.hos.service;

import com.transport.hos.model.DriverViolation;
import com.transport.hos.repository.DriverViolationRepository;
import com.transport.hos.response.DriverViolationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DriverViolationService {

    @Autowired
    private DriverViolationRepository driverViolationRepository;

    // Convert VO to Entity
    private DriverViolation convertToEntity(DriverViolationVO vo) {
        DriverViolation entity = new DriverViolation();
        entity.setUpdateTicket(vo.getUpdateTicket());
        entity.setViolationId(vo.getViolationId());
        entity.setTripId(vo.getTripId());
        entity.setViolationType(vo.getViolationType());
        entity.setCreatedDateTime(vo.getCreatedDateTime());
        entity.setUpdatedDateTime(vo.getUpdatedDateTime());
        entity.setAuuid(vo.getAuuid());
        entity.setCreatedUser(vo.getCreatedUser());
        entity.setUpdatedUser(vo.getUpdatedUser());
        entity.setViolationDescription(vo.getViolationDescription());
        entity.setRowId(vo.getRowId());
        return entity;
    }

    // Convert Entity to VO
    private DriverViolationVO convertToVO(DriverViolation entity) {
        DriverViolationVO vo = new DriverViolationVO();
        vo.setUpdateTicket(entity.getUpdateTicket());
        vo.setViolationId(entity.getViolationId());
        vo.setTripId(entity.getTripId());
        vo.setViolationType(entity.getViolationType());
        vo.setCreatedDateTime(entity.getCreatedDateTime());
        vo.setUpdatedDateTime(entity.getUpdatedDateTime());
        vo.setAuuid(entity.getAuuid());
        vo.setCreatedUser(entity.getCreatedUser());
        vo.setUpdatedUser(entity.getUpdatedUser());
        vo.setViolationDescription(entity.getViolationDescription());
        vo.setRowId(entity.getRowId());
        return vo;
    }

    // Create
    public DriverViolationVO addViolation(DriverViolationVO vo) {
        DriverViolation entity = convertToEntity(vo);
        return convertToVO(driverViolationRepository.save(entity));
    }

    // Read
    public DriverViolationVO getViolation(int violationId) {
        return driverViolationRepository.findById(violationId)
                .map(this::convertToVO)
                .orElse(null);
    }

    // Update
    public DriverViolationVO updateViolation(int id, DriverViolationVO vo) {
        if (driverViolationRepository.existsById(id)) {
            DriverViolation entity = convertToEntity(vo);
            return convertToVO(driverViolationRepository.save(entity));
        }
        return null;
    }

    // Delete
    public void deleteViolation(int violationId) {
        driverViolationRepository.deleteById(violationId);
    }

    // List All
    public List<DriverViolationVO> getAllViolations() {
        return StreamSupport.stream(driverViolationRepository.findAll().spliterator(), false)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
}

