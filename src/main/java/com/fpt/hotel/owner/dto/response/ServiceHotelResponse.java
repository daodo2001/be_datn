package com.fpt.hotel.owner.dto.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceHotelResponse {
    private Long id;

    private String name;

    private Double price;

    private String image;
}
