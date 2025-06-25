package com.transport.hos.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverLogs {

    private DriverStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
