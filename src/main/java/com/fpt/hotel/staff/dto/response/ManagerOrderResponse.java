package com.fpt.hotel.staff.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ManagerOrderResponse {
    private Long idBooking ;

    private String dateIn;

    private  String dateOut;

    private String fullName;

    private String phone;

    private Double totalPrice;

    private Double depositPrice;

    private String status;

    private String createDate;

    private String maGd;

    private String noiDungCk;

    private Integer totalRoom;

    private String nameHotel;

    private String nameTypeRoom;

    private String reason;

    private Long idTypeRoom;

    private Long idHotel;

    private Boolean checkUpdateBooking;

    private String imageCmnd;

    private Long idTransaction;

    private String tranDate ;

    private String amout ;

    private String soHoaDon ;

    private Date createDateCheck;

}
