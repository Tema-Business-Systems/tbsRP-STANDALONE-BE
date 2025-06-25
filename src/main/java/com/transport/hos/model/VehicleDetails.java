package com.transport.hos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetails {
    private List<Vehicles> vehicles;
    private ChartData drivingDistanceData;
    private ChartData drivingHoursData;
    private ChartData safetyData;
}
