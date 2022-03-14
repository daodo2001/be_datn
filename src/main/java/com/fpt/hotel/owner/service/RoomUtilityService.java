package com.fpt.hotel.owner.service;

import com.fpt.hotel.owner.dto.request.TypeRoomUtilityRequest;
import com.fpt.hotel.owner.dto.response.RoomUtilityResponse;

import java.util.List;

public interface RoomUtilityService {

    List<RoomUtilityResponse> saveTypeRoomUtitiliy(TypeRoomUtilityRequest typeRoomUtilityRequest);
}
