package com.transport.fleet.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.transaction.Transactional;
import java.util.Date;

@Data
@AllArgsConstructor
public class TransactionHistory {
    private Integer meter;
    private Date date;
    private String time;
    private String voucherNum;
    private Integer source;
}
