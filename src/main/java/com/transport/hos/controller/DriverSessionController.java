package com.transport.hos.controller;

import com.transport.hos.response.DriverSessionVO;
import com.transport.hos.service.DriverSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver-sessions")
public class DriverSessionController {

    @Autowired
    private DriverSessionService driverSessionService;

    // **ADD (CREATE) Endpoint**
    @PostMapping
    public DriverSessionVO createSession(@RequestBody DriverSessionVO vo) {
        return driverSessionService.saveSession(vo);
    }

    // **GET (READ) by ID Endpoint**
    @GetMapping("/{id}")
    public DriverSessionVO getSessionById(@PathVariable int id) {
        return driverSessionService.getSessionById(id);
    }

    // **GET (READ) all sessions Endpoint**
    @GetMapping
    public List<DriverSessionVO> getAllSessions() {
        return driverSessionService.getAllSessions();
    }

    // **UPDATE Endpoint**
    @PutMapping("/{id}")
    public DriverSessionVO updateSession(@PathVariable int id, @RequestBody DriverSessionVO vo) {
        return driverSessionService.updateSession(id, vo);
    }

    // **DELETE Endpoint**
    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable int id) {
        driverSessionService.deleteSession(id);
    }
}

