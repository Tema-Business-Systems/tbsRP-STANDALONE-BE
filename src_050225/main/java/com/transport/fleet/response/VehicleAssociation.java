package com.transport.fleet.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleAssociation {
    private String linkType;
    private String trailerOrEqp;
    private String description;
    private String weight;
    private String weightUOM;
    private String weightUOMDesc;
    private String volume;
    private String volumeUOM;
    private String volumeUOMDesc;
}
