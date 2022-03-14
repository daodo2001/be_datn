package com.fpt.hotel.staff.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundVnpayRequest {

    String vnp_TxnRef;

    String vnp_TransDate;

    String amountReq;

    boolean hoanHuy;

    Long idBooking;

    String reason ;

    Integer idUser;


}
