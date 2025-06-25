package com.transport.fleet.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRenewal {
    private String site;
    private String desc;
    private Double serviceTime;
}
