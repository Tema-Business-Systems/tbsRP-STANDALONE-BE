package com.transport.hos.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicles {
    private String vehicle;
    private String efficiency;
    private String fuelUsed;
    private String distance;
    private String estCarbonEmissions;
    private String estCost;
    private String totalEngineRunTime;
    private String idleTimePercentage;
}
