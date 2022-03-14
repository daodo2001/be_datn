package com.fpt.hotel.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "vn_pay_transactions")
public class VnpayTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String maGd;
    private String soHoaDon ;
    private Double soTien;
    private String bankCode;
    private String noidungCk;
    private String status;
    private String createDate;
    private Long idBooking;
}
