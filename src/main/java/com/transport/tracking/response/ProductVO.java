package com.transport.tracking.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ProductVO {

    private String productCode;
    private String productName;
    private String productCateg;
    private String quantity;
    private String uom;
    private String docLineNum;
    private String convQty;
    private String puu;
    private String weight;
    private String volume;
    private String wei_unit;
    private String vol_unit;

}
