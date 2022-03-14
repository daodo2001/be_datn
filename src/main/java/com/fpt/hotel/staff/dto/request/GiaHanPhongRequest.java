package com.fpt.hotel.staff.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GiaHanPhongRequest {

    private Long idBooking;

    private Date dateIn;

    private Date dateOut;

    private Integer tongNgay;

    private Long idTypeRoom;

}
