package com.transport.tracking.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@IdClass(UserAlignedSiteId.class)
@Table(name = "XX10CUSERD")
public class UserAlignedSite {


      @Column(name = "ROWID", insertable = false, updatable = false)
      private String rowid;  // Default system-managed value

      @Id
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "XAUS_0", referencedColumnName = "XAUS_0", nullable = false)
      @JsonBackReference  // Prevents recursion
      private User user;

      @Id
      @Column(name= "XFCY_0")
      private String fcy;

      @Column(name= "UPDTICK_0")
      private int updtick;




      @Column(name= "XLINNO_0")
      private int lineno;
      @Column(name= "XROUT_0")
      private int rout;
      @Column(name= "XMAPS_0")
      private int map;

      @Column(name= "XDEFFCY_0")
      private String defflg;
      @Column(name= "XEMAIL_0")
      private String email;
      @Column(name= "XPHN_0")
      private String phone;
      @Column(name= "XROLE_0")
      private String role;
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

}
