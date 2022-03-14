package com.fpt.hotel.staff.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateBookingRequest {
    private Long idBooking;

    private Long idHotel;

    private String ghiChu;

    private Long idTypeRoom;

    private Integer totalRoom;

    private Integer newDepositPrice;

    private Date dateIn;

    private Date dateOut;

    private Integer idChanger;

    private Double totalPrice;
}
