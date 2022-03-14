package com.fpt.hotel.owner.dto.response;

import com.fpt.hotel.model.TypeRoomImage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TypeRoomResponse {
    private Long id;

    private String name;

    private double price;

    private int capacity;

    private String description;

    private double size;

    private String status;

    List<TypeRoomImageResponse> typeRoomImages;
}
