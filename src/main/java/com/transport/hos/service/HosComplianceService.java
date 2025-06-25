package com.transport.hos.service;

import com.transport.hos.model.Driver;
import com.transport.hos.model.DriverLogs;
import com.transport.hos.model.DriverStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HosComplianceService {

    private static final int MAX_DRIVING_HOURS = 11;
    private static final int MAX_ON_DUTY_HOURS = 14;
    private static final int BREAK_AFTER_HOURS = 8;
    private static final int MIN_OFF_DUTY_HOURS = 10;
    private static final int WEEKLY_HOUR_LIMIT_7_DAYS = 60;
    private static final int WEEKLY_HOUR_LIMIT_8_DAYS = 70;
    private static final int RESET_HOURS = 34;
    private static final int REQUIRED_HOURS_FOR_TRIP = 8;

    public boolean isHosCompliant(List<DriverLogs> logs) {
        LocalDateTime lastOffDutyEnd = getLastOffDutyEnd(logs);
        Duration drivingDuration = getTotalDrivingHours(logs);
        Duration onDutyDuration = getTotalOnDutyHours(logs);
        Duration weeklyHours = getWeeklyHours(logs);

        boolean drivingTimeRule = drivingDuration.toHours() <= MAX_DRIVING_HOURS;
        boolean onDutyTimeRule = onDutyDuration.toHours() <= MAX_ON_DUTY_HOURS;
        boolean offDutyRule = lastOffDutyEnd != null && Duration.between(lastOffDutyEnd, LocalDateTime.now()).toHours() >= MIN_OFF_DUTY_HOURS;
        boolean weeklyLimitRule = weeklyHours.toHours() <= WEEKLY_HOUR_LIMIT_7_DAYS || weeklyHours.toHours() <= WEEKLY_HOUR_LIMIT_8_DAYS;
        boolean resetRule = hasCompletedReset(logs);

        return drivingTimeRule && onDutyTimeRule && offDutyRule && weeklyLimitRule && resetRule;
    }

    public List<Driver> getEligibleDrivers(List<Driver> drivers) {
        /*return drivers.stream()
                .filter(driver -> hasEnoughDrivingTime(driver.getLogs()))
                .collect(Collectors.toList());*/
        return null;
    }

    private boolean hasEnoughDrivingTime(List<DriverLogs> logs) {
        Duration remainingDrivingTime = Duration.ofHours(MAX_DRIVING_HOURS).minus(getTotalDrivingHours(logs));
        return remainingDrivingTime.toHours() >= REQUIRED_HOURS_FOR_TRIP;
    }

    private Duration getTotalDrivingHours(List<DriverLogs> logs) {
        return logs.stream()
                .filter(log -> log.getStatus() == DriverStatus.DRIVING)
                .map(log -> Duration.between(log.getStartTime(), log.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    private Duration getTotalOnDutyHours(List<DriverLogs> logs) {
        return logs.stream()
                .filter(log -> log.getStatus() == DriverStatus.ON_DUTY || log.getStatus() == DriverStatus.DRIVING)
                .map(log -> Duration.between(log.getStartTime(), log.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    private Duration getWeeklyHours(List<DriverLogs> logs) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        return logs.stream()
                .filter(log -> log.getStartTime().isAfter(oneWeekAgo))
                .map(log -> Duration.between(log.getStartTime(), log.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    private LocalDateTime getLastOffDutyEnd(List<DriverLogs> logs) {
        return logs.stream()
                .filter(log -> log.getStatus() == DriverStatus.OFF_DUTY)
                .map(DriverLogs::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    private boolean hasCompletedReset(List<DriverLogs> logs) {
        return logs.stream()
                .filter(log -> log.getStatus() == DriverStatus.OFF_DUTY)
                .anyMatch(log -> Duration.between(log.getStartTime(), log.getEndTime()).toHours() >= RESET_HOURS);
    }
}
