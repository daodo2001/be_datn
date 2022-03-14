package com.fpt.hotel.owner.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelRequest {
    private Long id;
    private String name;
    private String city;
    private String address;
    private Integer totalNumberRoom;
    private Integer isEnabled;
    private String images;


}
