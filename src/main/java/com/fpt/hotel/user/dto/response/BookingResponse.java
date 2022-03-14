package com.fpt.hotel.user.dto.response;

import com.fpt.hotel.model.Transaction_Info;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BookingResponse {
    private Long id;

    private Date create_date;

    private Double deposit_price;

    private String full_name;

    private String email;

    private String phone;

    private double deposit;

    private int id_card;

    private String status;

    private double discount;

    private Long id_user;

    private Long id_voucher;

    private List<BookingCheckInCheckOutResponse> checkinCheckouts;

    private List<Transaction_Info> id_transaction_info;

    private Long id_hotel;

    private Double totalPrice;

}
