package com.fpt.hotel.staff.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelRoom {

    private Long idBooking ;

    private String status;

    private String reason;

    private String [] statusCodeHoanHuy;
}
