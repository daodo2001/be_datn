package com.fpt.hotel.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingOrderResponse {

    private Long idBooking;

    private String nameHotel ;

    private String nameTypeRoom;

    private String dateIn;

    private String dateOut;

    private Double price;

    private String status;

    private Integer totalRoom;

}
