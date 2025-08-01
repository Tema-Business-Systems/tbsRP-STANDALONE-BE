package com.transport.fleet.model;

import lombok.Data;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Date;

@Data
@Entity
@Table(name = "XX10CVEHICUL")
public class FleetVehicle {
    @Id
    @Column(name = "ROWID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rowId;
    @Column(name = "UPDTICK_0")
    private Integer updTick;
    @Column(name = "CODEYVE_0")
    private String codeyve;
    @Column(name = "NAME_0")
    private String name;
    @Column(name = "STARTDEPOTN_0")
    private String startdepotn;
    @Column(name = "ENDDEPOTNAME_0")
    private String enddepotname;
    @Column(name = "STARTDEPOTS_0")
    private Double startdepots;
    @Column(name = "ENDDEPOTSERV_0")
    private Double enddepotserv;
    @Column(name = "EARLIESTSTAR_0")
    private String earliestStart;
    @Column(name = "LATESTSTARTT_0")
    private String latestStart;
    @Column(name = "ARRIVEDEPART_0")
    private String arriveDepart;
    @Column(name = "CAPACITIES_0")
    private Double capacities;
    @Column(name = "FIXEDCOST_0")
    private Double fixedCost;
    @Column(name = "COSTPERUNITT_0")
    private Double costPerUnitT;
    @Column(name = "COSTPERUNITD_0")
    private Double costPerUnitD;
    @Column(name = "OVERTIMESTAR_0")
    private Double overtTimeStart;
    @Column(name = "COSTPERUNITO_0")
    private Double costPerUnitO;
    @Column(name = "MAXORDERCOU_0")
    private int maxOrderCOU;
    @Column(name = "MAXTOTALTIME_0")
    private Double maxTotalTime;
    @Column(name = "MAXTOTALTRAV_0")
    private Double maxTotalTrav;
    @Column(name = "MAXTOTALDIST_0")
    private Double maxTotalDist;
    @Column(name = "SPECIALTYNAM_0")
    private String specialtynam;
    @Column(name = "ASSIGNMENTRU_0")
    private String assignmentru;
    @Column(name = "FCY_0")
    private String fcy;
    @Column(name = "BPTNUM_0")
    private String bptNum;
    @Column(name = "VOL_0")
    private Double vol;
    @Column(name = "VOU_0")
    private String vou;
    @Column(name = "CREDATTIM_0")
    private Date createDateTime;
    @Column(name = "UPDDATTIM_0")
    private Date updateDateTime;
    @Column(name = "AUUID_0")
    private byte[] auuid;
    @Column(name = "CREUSR_0")
    private String createUser;
    @Column(name = "UPDUSR_0")
    private String updateUser;
    @Column(name = "DEPOTNAME_0")
    private String depotName0;
    @Column(name = "DEPOTNAME_1")
    private String depotName1;
    @Column(name = "DEPOTNAME_2")
    private String depotName2;
    @Column(name = "DEPOTNAME_3")
    private String depotName3;
    @Column(name = "DEPOTNAME_4")
    private String depotName4;
    @Column(name = "DEPOTNAME_5")
    private String depotName5;
    @Column(name = "DEPOTNAME_6")
    private String depotName6;
    @Column(name = "DEPOTNAME_7")
    private String depotName7;
    @Column(name = "DEPOTNAME_8")
    private String depotName8;
    @Column(name = "DEPOTNAME_9")
    private String depotName9;
    @Column(name = "SERVICETIME_0")
    private Double serviceTime0;
    @Column(name = "SERVICETIME_1")
    private Double serviceTime1;
    @Column(name = "SERVICETIME_2")
    private Double serviceTime2;
    @Column(name = "SERVICETIME_3")
    private Double serviceTime3;
    @Column(name = "SERVICETIME_4")
    private Double serviceTime4;
    @Column(name = "SERVICETIME_5")
    private Double serviceTime5;
    @Column(name = "SERVICETIME_6")
    private Double serviceTime6;
    @Column(name = "SERVICETIME_7")
    private Double serviceTime7;
    @Column(name = "SERVICETIME_8")
    private Double serviceTime8;
    @Column(name = "SERVICETIME_9")
    private Double serviceTime9;
    @Column(name = "ALLDRIVER_0")
    private int allDriver;
    @Column(name = "UVYCOD_0")
    private String uvycod;
    @Column(name = "STYZON_0")
    private String styzon;
    @Column(name = "LONGUEUR_0")
    private int longueur;
    @Column(name = "LARGEUR_0")
    private int largeur;
    @Column(name = "DIMLARG1_0")
    private String dimlarg1;
    @Column(name = "DIMLARG2_0")
    private String dimlarg2;
    @Column(name = "DIMLARG3_0")
    private String dimlarg3;
    @Column(name = "DIMLARG4_0")
    private String dimlarg4;
    @Column(name = "DIMLARG5_0")
    private String dimlarg5;
    @Column(name = "DIMLARG6_0")
    private String dimlarg6;
    @Column(name = "SECTORID_0")
    private String sectorId;
    @Column(name = "OWNERSHIP_0")
    private int ownerShip;
    @Column(name = "PRIMMET_0")
    private int primmet;
    @Column(name = "SECDMET_0")
    private int secdmet;
    @Column(name = "REFERENCE_0")
    private String reference;
    @Column(name = "LASTINSP_0")
    private Date lastinsp;
    @Column(name = "INSPEXP_0")
    private Date inspexp;
    @Column(name = "GPSID_0")
	private String gpsId;
    @Column(name = "MOBTRAC_0")
	private String mobtrac;
    @Column(name = "MOBRAD_0")
	private String mobrad;
    @Column(name = "FIREEXT_0")
	private String fireExit;
    @Column(name = "LICREF_0")
    private String licref;
    @Column(name = "LICEXP_0")
	private Date licexp;
    @Column(name = "LICNOT_0")
	private String licnot;
    @Column(name = "VENDOR_0")
	private String vendor;
    @Column(name = "INSEXP_0")
    private Date insexp;
    @Column(name = "INSREF_0")
	private String insref;
    @Column(name = "INSNOT_0")
	private String insnot;
    @Column(name = "AASREF_0")
	private String aasref;
    @Column(name = "CATEGO_0")
    private String category;
    @Column(name = "BRAND_0")
    private int brand;
    @Column(name = "MODEL_0")
	private String model;
    @Column(name = "VYEAR_0")
	private int vYear;
    @Column(name = "COLOR_0")
	private int color;
    @Column(name = "FUELTYP_0")
	private int fuelType;
    @Column(name = "ENGICC_0")
	private int engicc;
    @Column(name = "CHASNUM_0")
	private String chasnum;
    @Column(name = "INSYEAR_0")
    private int insYear;
    @Column(name = "ROAYEAR_0")
    private int roaYear;
    @Column(name = "CO2EM_0")
	private Double co2em;
    @Column(name = "DRIVERID_0")
	private String driverId0;
    @Column(name = "DRIVERID_1")
    private String driverId1;
    @Column(name = "DRIVERID_2")
    private String driverId2;
    @Column(name = "DRIVERID_3")
    private String driverId3;
    @Column(name = "DRIVERID_4")
    private String driverId4;
    @Column(name = "DRIVERID_5")
    private String driverId5;
    @Column(name = "DRIVERID_6")
    private String driverId6;
    @Column(name = "DRIVERID_7")
    private String driverId7;
    @Column(name = "DRIVERID_8")
    private String driverId8;
    @Column(name = "DRIVERID_9")
    private String driverId9;
    @Column(name = "EMPTMASS_0")
	private Double emptmass;
    @Column(name = "GROSMASS_0")
    private Double gromass;
    @Column(name = "POURTOL_0")
	private Double pourtol;
    @Column(name = "TCLCOD_0")
	private String tclcod0;
    @Column(name = "TCLCOD_1")
    private String tclcod1;
    @Column(name = "TCLCOD_2")
    private String tclcod2;
    @Column(name = "TCLCOD_3")
    private String tclcod3;
    @Column(name = "TCLCOD_4")
    private String tclcod4;
    @Column(name = "TCLCOD_5")
    private String tclcod5;
    @Column(name = "TCLCOD_6")
    private String tclcod6;
    @Column(name = "TCLCOD_7")
    private String tclcod7;
    @Column(name = "TCLCOD_8")
    private String tclcod8;
    @Column(name = "TCLCOD_9")
    private String tclcod9;
    @Column(name = "ALLTCLCOD_0")
	private int allTclCod;
    @Column(name = "BPCNUM_0")
	private String bpcNum0;
    @Column(name = "BPCNUM_1")
    private String bpcNum1;
    @Column(name = "BPCNUM_2")
    private String bpcNum2;
    @Column(name = "BPCNUM_3")
    private String bpcNum3;
    @Column(name = "BPCNUM_4")
    private String bpcNum4;
    @Column(name = "BPCNUM_5")
    private String bpcNum5;
    @Column(name = "BPCNUM_6")
    private String bpcNum6;
    @Column(name = "BPCNUM_7")
    private String bpcNum7;
    @Column(name = "BPCNUM_8")
    private String bpcNum8;
    @Column(name = "BPCNUM_9")
    private String bpcNum9;
    @Column(name = "ALLBPCNUM_0")
	private int allBPCNum;
    @Column(name = "TRAILER_0")
	private String trailer;
    @Column(name = "SDHTYP_0")
	private String sdhtyp;
    @Column(name = "XLOC_0")
    private String xloc;
    @Column(name = "XLOCTYP_0")
    private String xloctyp;
    @Column(name = "XVEHTYP_0")
	private String xVehTyp;
    @Column(name = "XISSEMIRATE_0")
    private String xissemirate;
    @Column(name = "XPER_0")
    private int xper;
    @Column(name = "QTY_0")
    private Double qty;
    @Column(name = "XHELPER_0")
    private String xhelper;
    @Column(name = "XSLMAN_0")
    private String xslman;
    @Column(name = "XTECHN_0")
    private String xtechn;
    @Column(name = "XACVFLG_0")
    private int xacvflg;
    @Column(name = "XTSICOD_0")
    private String xtsicod;
    @Column(name = "XTSICOD_1")
    private String xtsicod1;
    @Column(name = "XTSICOD_2")
    private String xtsicod2;
    @Column(name = "XTSICOD_3")
    private String xtsicod3;
    @Column(name = "XTSICOD_4")
    private String xtsicod4;
    @Column(name = "XTSICOD_5")
    private String xtsicod5;
    @Column(name = "XTSICOD_6")
    private String xtsicod6;
    @Column(name = "XTSICOD_7")
    private String xtsicod7;
    @Column(name = "XTSICOD_8")
    private String xtsicod8;
    @Column(name = "XTSICOD_9")
    private String xtsicod9;
    @Column(name = "XBUS2_0")
    private String xbus2;
    @Column(name = "XTOLLERANCE_0")
    private Double xtolerance;
    @Column(name = "XX10CLICTYPE_0")
    private int xx10clictype;
    @Column(name = "XCURALLDRI_0")
    private String xcuralldri;
    @Column(name = "XDATE_0")
    private Date xdate;
    @Column(name = "XTIME_0")
    private String xtime;
    @Column(name = "XCODOMETER_0")
    private int xcodometer;
    @Column(name = "XX10CSERCODE_0")
    private String xx10csercode;
    @Column(name = "XALLDRIVER_0")
    private String xalldriver;
    @Column(name = "XINSMAN_0")
    private int xinsman;
    @Column(name = "XCURMTRE_0")
    private Double xcurmtre;
    @Column(name = "XDELINSPEC_0")
    private String xdelinspec;
    @Column(name = "XRETINSPEC_0")
    private String xretinspec;
    @Column(name = "XMETER_0")
    private Double xmeter;
    @Column(name = "XLASTDATE_0")
    private Date xlastdate;
    @Column(name = "XLASTTIME_0")
    private String xlasttime;
    @Column(name = "XMTUNT_0")
    private String xmtunt;
    @Column(name = "CUR_0")
    private String cur;
    @Column(name = "XUNITS3_0")
    private String xunits3;
    @Column(name = "XCODOMETER1_0")
    private int xcodometer1;
    @Column(name = "XLASTDATE1_0")
    private Date xlastdate1;
    @Column(name = "XLASTTIME1_0")
    private String xlasttime1;
    @Column(name = "LASTINSP1_0")
    private Date lastinsp1;
    @Column(name = "XVFCAP_0")
    private Double xvfcap;
    @Column(name = "XVFCU_0")
    private String xvfcu;
    @Column(name = "XHGTH_0")
    private Double xhgth;
    @Column(name = "XLENGTH_0")
    private Double xlength;
    @Column(name = "XWIDTH_0")
    private Double xwidth;
    @Column(name = "XYEAROFMAN_0")
    private int xyearofman;
    @Column(name = "XOPERATION_0")
    private int xoperation;
    @Column(name = "XNBPALLET_0")
    private int xnbpallet;
    @Column(name = "XBATHGHT_0")
    private Double xbathght;
    @Column(name = "XGNDOCC_0")
    private Double xgndocc;
    @Column(name = "XLOADBAY_0")
    private int xloadbay;
    @Column(name = "XTAILGATE_0")
    private int xtailgate;
    @Column(name = "XVOL_0")
    private String xvol;
    @Column(name = "XWEU_0")
    private String xweu;
    @Column(name = "XMAXTOTALDIS_0")
    private String xmaxtotaldis;
    @Column(name = "XEQUIPID_0")
    private String xequipid;
    @Column(name = "XEQUIPID_1")
    private String xequipid1;
    @Column(name = "XEQUIPID_2")
    private String xequipid2;
    @Column(name = "XEQUIPID_3")
    private String xequipid3;
    @Column(name = "XEQUIPID_4")
    private String xequipid4;
    @Column(name = "XMAXSPEED_0")
    private Double xmaxspeed;
    @Column(name = "X10CSKILLCRI_0")
    private String x10cskillcri;
    @Column(name = "XINSPTYP_0")
    private String xinsptyp0;
    @Column(name = "XINSPTYP_1")
    private String xinsptyp1;
    @Column(name = "XINSPTYP_2")
    private String xinsptyp2;
    @Column(name = "XINSPTYP_3")
    private String xinsptyp3;
    @Column(name = "XLSTCHK_0")
    private Date xlstchk0;
    @Column(name = "XLSTCHK_1")
    private Date xlstchk1;
    @Column(name = "XLSTCHK_2")
    private Date xlstchk2;
    @Column(name = "XLSTCHK_3")
    private Date xlstchk3;
    @Column(name = "XPERIODICITY_0")
    private int xperiodicity0;
    @Column(name = "XPERIODICITY_1")
    private int xperiodicity1;
    @Column(name = "XPERIODICITY_2")
    private int xperiodicity2;
    @Column(name = "XPERIODICITY_3")
    private int xperiodicity3;
    @Column(name = "XNEXTVISIT_0")
    private Date xnextvisit0;
    @Column(name = "XNEXTVISIT_1")
    private Date xnextvisit1;
    @Column(name = "XNEXTVISIT_2")
    private Date xnextvisit2;
    @Column(name = "XNEXTVISIT_3")
    private Date xnextvisit3;
    @Column(name = "XTYPEIN_0")
    private int xtypein0;
    @Column(name = "XTYPEIN_1")
    private int xtypein1;
    @Column(name = "XTYPEIN_2")
    private int xtypein2;
    @Column(name = "XTYPEIN_3")
    private int xtypein3;
    @Column(name = "XDRN_0")
    private int xdrn;
    @Column(name = "X1COVERHRS_0")
    private int x1coverhrs;
    @Column(name = "EQUIPNOT_0")
    private String equipnot;

    @PrePersist
    @PreUpdate
    public void setDefaultValues() throws IllegalAccessException {
        Field[] columns = this.getClass().getDeclaredFields();
        for(Field column: columns){
            column.setAccessible(true);
            if(column.get(this)==null){
                if(column.getType().equals(String.class)){
                    column.set(this, "");
                }else if(column.getType().equals(Integer.class)){
                    column.set(this, 0);
                }else if(column.getType().equals(Double.class)){
                    column.set(this, 0.0);
                }else if(column.getType().equals(Date.class)){
                    column.set(this, new Date());
                }else if(column.getType().equals(byte[].class)){
                    column.set(this, new byte[]{0});
                }
            }
        }
    }

}
