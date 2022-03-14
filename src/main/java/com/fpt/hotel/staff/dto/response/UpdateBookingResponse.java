package com.fpt.hotel.staff.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookingResponse {
    private Long id;

    private String dateIn;

    private String dateOut;

    private String nameTypeRoom;

    private Integer totalRoom;

    private Double newDepositPrice;

    private Boolean checkUpdateBooking;

    private Double newTotalPrice;

}
