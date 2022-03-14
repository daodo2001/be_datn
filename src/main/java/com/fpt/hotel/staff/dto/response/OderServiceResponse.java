package com.fpt.hotel.staff.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OderServiceResponse {

    private Long id;

    private Integer quantity;

    private Long idRoom;

    private Long idTransaction;

    private Long idServiceHotel;
}
