package com.fpt.hotel.staff.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingHistoryResponse {
    private String fullName;

    private String phone;

    private String dateIn;

    private String dateOut;

    private String nameTypeRoom;

    private String newNameTypeRoom;

    private String newDateIn;

    private String newDateOut;

    private Integer totalRoom;

    private Integer newTotalRoom;

    private String status;

    private String username;

    private String createDate;

    private String nameHotel;

}
