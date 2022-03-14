package com.fpt.hotel.owner.service;
import com.fpt.hotel.owner.dto.response.ServiceHotelResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ServiceHotelService {

    List<ServiceHotelResponse> findAll();

    ServiceHotelResponse save(String folder, String serviceHotel, List<MultipartFile> files);

    ServiceHotelResponse update(String folder, String serviceHotel, List<MultipartFile> files);
}
