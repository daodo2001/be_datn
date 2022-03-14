package com.fpt.hotel.user.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class CheckInCheckOutRequest {

    private Long id;

    private Date date_in;

    private Date date_out;

    private Long idBooking;
    
    private Long idTypeRoom;
}
