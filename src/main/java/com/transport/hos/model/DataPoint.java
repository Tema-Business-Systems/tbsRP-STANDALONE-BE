package com.transport.hos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataPoint {

    private String start;
    private String end;
    private String duration;
    private String status;
}
