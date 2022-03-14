package com.fpt.hotel.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VnpayTransactionResponse {
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
