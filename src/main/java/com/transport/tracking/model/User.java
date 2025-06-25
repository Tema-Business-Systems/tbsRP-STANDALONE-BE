package com.transport.tracking.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "XX10CUSERS")
public class User {

      @Column(name= "UPDTICK_0")
      private int updtick;
      @Id
      @Column(name= "XAUS_0")
      private String xlogin;
      @Column(name= "XPWSD_0")
      private String xpswd;
      @Column(name= "XAUSNA_0")
      private String xusrname;
      @Column(name= "XACT_0")
      private int xact;
      @Column(name= "CREDATTIM_0")
      private Date credattim;
      @Column(name= "UPDDATTIM_0")
      private Date upddattim;
      @Column(name= "AUUID_0")
      private byte[] auuid;
      @Column(name= "CREUSR_0")
      private String creusr;
      @Column(name= "UPDUSR_0")
      private String updusr;

      @Column(name= "XEMAIL_0")
      private String email;
      @Column(name= "XPHN_0")
      private String phone;
      @Column(name= "XTEL_0")
      private String tel;
      @Column(name= "XLANMAIN_0")
      private String lngmain;
      @Column(name= "XLANSEC_0")
      private String lansec;
      @Column(name= "BPAADDLIG_0")
      private String bpadd;
      @Column(name= "BPAADDLIG_1")
      private String bpadd1;
      @Column(name= "BPAADDLIG_2")
      private String bpadd2;
      @Column(name= "POSCOD_0")
      private String pincode;
      @Column(name= "CTY_0")
      private String city;
      @Column(name= "SAT_0")
      private String state;
      @Column(name= "CRY_0")
      private String country;
      @Column(name= "XRPFLG_0")
      private int routeplannerflg;
      @Column(name= "XSCHFLG_0")
      private int schedulerflg;
      @Column(name= "XCALVFLG_0")
      private int calendarrpflg;
      @Column(name= "XMAPVFLG_0")
      private int mapviewrpflg;
      @Column(name= "XSCRRTFLG_0")
      private int screportsflg;
      @Column(name= "XFLEETFLG_0")
      private int fleetmgmtflg;
      @Column(name= "XUSRMGMTFLG_0")
      private int usermgmtflg;
      @Column(name= "XADDPCKFLG_0")
      private int addPicktcktflg;
      @Column(name= "XRMPCKFLG_0")
      private int removePicktcktflg;

      @Column(name = "ROWID", insertable = false, updatable = false)
      private String rowid;

      public User() {
      }

      @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
      @JsonManagedReference  // Prevents recursion
      private List<UserAlignedSite> alignedSites;

      /*@ManyToMany(cascade = { CascadeType.ALL })
      @JoinTable(
              name = "XX10CUSRROL",
              joinColumns = { @JoinColumn(name = "XUSER_0") },
              inverseJoinColumns = { @JoinColumn(name = "XROLE_0") }
      )
      private Set<Role> roles = new HashSet<>();*/
}
