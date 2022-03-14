package com.fpt.hotel.owner.service;

import com.fpt.hotel.owner.dto.request.HotelTypeRoomRequest;
import com.fpt.hotel.owner.dto.response.HotelTypeRoomPublicResponse;
import com.fpt.hotel.owner.dto.response.HotelTypeRoomResponse;

import java.util.List;

public interface IHotelTypeRoomService {

    HotelTypeRoomResponse save(HotelTypeRoomRequest hotelTypeRoomRequest);

    List<HotelTypeRoomResponse> findAll(Long idHotel);

    List<HotelTypeRoomResponse> checkHotelTypeRoom(HotelTypeRoomRequest hotelTypeRoomRequest);

    List<HotelTypeRoomPublicResponse> hotelTypeRoomPublicResponses(Long idHotel, Long idTypeRoom,
                                                                   String dateIn, String DateOut);

    HotelTypeRoomResponse update(HotelTypeRoomRequest hotelTypeRoomRequest);
}
