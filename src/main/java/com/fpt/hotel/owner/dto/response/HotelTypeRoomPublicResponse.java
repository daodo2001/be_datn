package com.fpt.hotel.owner.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelTypeRoomPublicResponse {

    private Integer id;

    private Integer totalNumberRoom;

    private Long idHotel;

    private TypeRoomResponse typeRoom;
}
