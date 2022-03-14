package com.fpt.hotel.owner.service;

import com.fpt.hotel.model.Room;
import com.fpt.hotel.owner.dto.response.RoomResponse;

import java.util.List;

public interface IRoomService {
    List<RoomResponse> findAll(Long idHotel , Long idTypeRoom);

    RoomResponse save(Room room);

    RoomResponse updateEnabled(Long id);

}
