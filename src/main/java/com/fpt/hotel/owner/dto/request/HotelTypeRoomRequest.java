package com.fpt.hotel.owner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelTypeRoomRequest {
    private Integer id;

    private Integer totalNumberRoom;

    private Long idHotel;

    private Long idTypeRoom;
}
