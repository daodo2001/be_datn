package com.fpt.hotel.owner.dto.response;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherResponse {
	private Long id;
    private String name;
    private String code;
    
    @Temporal(TemporalType.DATE)
    private Date startTime;

    private Date endTime;

    private String description;

    private Integer status;
    private String nameTypeVoucher;
}
