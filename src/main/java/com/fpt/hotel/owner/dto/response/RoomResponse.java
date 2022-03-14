package com.fpt.hotel.owner.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomResponse {
    private Long id;

    private String numberRoom;

    private String status;

    private Boolean enabled;

    private String description;

    private String nameTypeRoom;

    private List<String> imagesTienIch;


}
