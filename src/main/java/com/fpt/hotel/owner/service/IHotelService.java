package com.fpt.hotel.owner.service;

import com.fpt.hotel.model.Hotel;
import com.fpt.hotel.owner.dto.request.HotelRequest;
import com.fpt.hotel.owner.dto.response.HotelResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IHotelService {

    List<HotelResponse> findAllHotels();


    HotelResponse createHotel(String folder, String hotel, List<MultipartFile>  files);

    HotelResponse updateHotel(Long id ,String folder, String hotel, List<MultipartFile>  files);

    HotelResponse findById(Long id);

    HotelResponse updateIsEnabled(Long id);

    HotelResponse updateHotel(HotelRequest hotelRequest);
}
