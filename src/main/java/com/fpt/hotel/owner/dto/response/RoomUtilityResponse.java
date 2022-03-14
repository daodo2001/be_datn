package com.fpt.hotel.owner.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomUtilityResponse {
    private Long id;

    private Long idRoom;

    private Integer quantity;

    private Long idUtility;
}
