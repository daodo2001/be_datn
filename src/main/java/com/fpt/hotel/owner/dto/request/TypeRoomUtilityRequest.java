package com.fpt.hotel.owner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeRoomUtilityRequest {
    private Long idTienIch;

    private Long idHotel;

    private Long idTypeRoom;
}
