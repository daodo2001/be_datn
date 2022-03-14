package com.fpt.hotel.staff.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class TransactionInfoResponse {
    private Long id;

    private Date date_release;

    private double total_price;

    private String status;

    private Integer id_creator;

    private Long idBooking;

    private String description;
}
