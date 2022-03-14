package com.fpt.hotel.staff.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiaHanPhongResponse {

    private String dateIn;

    private String dateOut;

    private Double totalPrice;
}
