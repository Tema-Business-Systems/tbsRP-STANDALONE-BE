package com.transport.fleet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostalCodeDetails {
    private String country;
    private String postalCode;
    private String city;
    private String subDivision;
}
