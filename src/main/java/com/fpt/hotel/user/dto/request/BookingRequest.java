package com.fpt.hotel.user.dto.request;

import com.fpt.hotel.user.dto.response.BookingCheckInCheckOutResponse;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class BookingRequest {

    private Long id;

    @CreationTimestamp
    private Date create_date;

    private double deposit_price;

    private String full_name;

    private String email;

    private String phone;

    private double deposit;

    private String status;

    private Long id_user;

    private Long id_voucher;

    private CheckInCheckOutRequest checkInCheckOutRequest;

    private Long id_hotel;

    private Double totalPrice;

    private Integer totalPeoples;

    private Integer tongNgay;

    private Integer tongSoPhong;
}
