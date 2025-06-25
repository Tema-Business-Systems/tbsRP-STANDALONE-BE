package com.transport.tracking.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSiteVO {
      private String user;
      private String fcy;
      private String cur;
      private String distunit;
      private String volunit;
      private String massunit;
      private String fcynam;
      private String cry;
      private String xx10c_geox;
      private String xx10c_geoy;
      private int fcyNumber;
      private int rowid;
      private String driverDayShift;
      private String driverWeekShift;
      private String driverBreakDuration;
      private String drivingHrsBtwBreak;
      private String totalDrivingHrsPerDay;
      private String totalWorkingHrsPerDay;

}
