package com.transport.fleet.response;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
public class VehicleClassVO {
    private Long rowid;
    private String className;
    private String desc;
    private int enaFlag;
    private String cry;
    private int typ;
    private int axlnbr;
    private Double  xmaxcapw;
    private Double xmaxcapv;
    private Date createDateTime;
    private Date updateDateTime;
    private byte[] auuid;
    private String creusr;
    private String updusr;
    private String xmaxvunit;
    private String xmaxunit;
    private int xskillno;
    private String xinspin;
    private int xmanin;
    private String xinspout;
    private int xmanout;
    private List<VehicleAssociation> associationList;
    private byte[] image;
}
