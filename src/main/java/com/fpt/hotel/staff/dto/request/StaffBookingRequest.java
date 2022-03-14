package com.fpt.hotel.staff.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StaffBookingRequest {

    private Long idUser;

    private String fullName;

    private Long idTypeRoom ;

    private String phone;

    private String email;

    private Double depositPrice;

    private Integer totalRoom;

    private Long idHotel;

    private Date dateIn;

    private Date dateOut;

    private Double totalPrice;

    private Boolean isOnline;

    private String ghiChu;
}
