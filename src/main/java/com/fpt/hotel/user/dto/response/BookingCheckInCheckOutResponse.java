package com.fpt.hotel.user.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class BookingCheckInCheckOutResponse {
    private Long id;

    private Date date_in;

    private Date date_out;

    private Long idBooking ;

    private Long idTypeRoom;
}
