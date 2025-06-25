package com.transport.fleet.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrailerCompartment {
    private String trailer;
    private Integer lineNum;
    private String compartment;
    private Double capacity;
}
