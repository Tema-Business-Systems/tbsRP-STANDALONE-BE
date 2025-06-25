package com.transport.hos.controller;

import com.transport.hos.response.DriverViolationVO;
import com.transport.hos.service.DriverViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/violations")
public class DriverViolationController {

    @Autowired
    private DriverViolationService driverViolationService;

    @PostMapping
    public DriverViolationVO addViolation(@RequestBody DriverViolationVO vo) {
        return driverViolationService.addViolation(vo);
    }

    @GetMapping("/{id}")
    public DriverViolationVO getViolation(@PathVariable int id) {
        return driverViolationService.getViolation(id);
    }

    @PutMapping("/{id}")
    public DriverViolationVO updateViolation(@PathVariable int id, @RequestBody DriverViolationVO vo) {
        return driverViolationService.updateViolation(id, vo);
    }

    @DeleteMapping("/{id}")
    public void deleteViolation(@PathVariable int id) {
        driverViolationService.deleteViolation(id);
    }

    @GetMapping
    public List<DriverViolationVO> getAllViolations() {
        return driverViolationService.getAllViolations();
    }
}

