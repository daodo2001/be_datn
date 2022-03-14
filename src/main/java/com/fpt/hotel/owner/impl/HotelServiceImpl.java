package com.fpt.hotel.owner.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.hotel.model.Hotel;
import com.fpt.hotel.owner.dto.request.HotelRequest;
import com.fpt.hotel.owner.dto.response.HotelResponse;
import com.fpt.hotel.owner.service.IHotelService;
import com.fpt.hotel.repository.HotelRepository;
import com.fpt.hotel.repository.RoomRepository;
import com.fpt.hotel.service.FileManagerService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements IHotelService {

    Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);
    @Autowired
    FileManagerService fileManagerService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    RoomRepository roomRepository;

    @Override
    public List<HotelResponse> findAllHotels() {

        logger.info("Find all data: " + hotelRepository.findAll());
        return hotelRepository.findAll().stream().map(hotel -> modelMapper.map(hotel, HotelResponse.class
        )).collect(Collectors.toList());
    }

    @Override
    public HotelResponse createHotel(String folder, String hotel, List<MultipartFile> files) {
        Hotel hotelJson = new Hotel();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            hotelJson = objectMapper.readValue(hotel, Hotel.class);
            hotelJson.setIsEnabled(1);
            String fileName = "";
            Boolean existsByName = hotelRepository.existsByName(hotelJson.getName());

            if (existsByName) {
                return null;
            }
            if (files != null) {
                List<String> fileLists = fileManagerService.save(folder, files);
                fileName = String.join("," , fileLists);
            }

            hotelJson.setImages(fileName);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

       return modelMapper.map( hotelRepository.save(hotelJson) , HotelResponse.class);

    }

    @Override
    public HotelResponse updateHotel(Long id, String folder, String hotel, List<MultipartFile> files) {
        Hotel hotelOld = hotelRepository.findById(id).get();
        Hotel hotelJson = new Hotel();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            hotelJson = objectMapper.readValue(hotel, Hotel.class);

            // lấy id cũ để cập nhật
            hotelJson.setId(hotelOld.getId());

            String image = "";
            System.out.println(files);
            if (files == null) {
                image = hotelOld.getImages();
            } else {
                List<String> fileLists = fileManagerService.save(folder, files);
                String fileName =  String.join("," , fileLists);
                fileManagerService.delete(folder, image);
                image = fileName;
            }

            hotelJson.setImages(image);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return modelMapper.map( hotelRepository.save(hotelJson) , HotelResponse.class);
    }

    @Override
    public HotelResponse findById(Long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        HotelResponse hotelResponse = null;
        if(hotelOptional.isPresent()){
            hotelResponse = modelMapper.map(hotelOptional.get(), HotelResponse.class);
        }
        return hotelResponse;
    }

    @Override
    public HotelResponse updateIsEnabled(Long id) {
        Optional<Hotel> hotelOptional =  hotelRepository.findById(id);
        if(hotelOptional.isEmpty()){
            return null;
        }
        Hotel hotel = hotelOptional.get();
        hotel.setIsEnabled(hotel.getIsEnabled() == 1 ? 0 : 1);
        Hotel newHotel = hotelRepository.save(hotel);
        return modelMapper.map(newHotel ,HotelResponse.class);

    }

    @Override
    public HotelResponse updateHotel(HotelRequest hotelRequest) {
        HotelResponse hotelResponse = null;
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelRequest.getId());
        if(hotelOptional.isPresent()){
            Hotel hotel = hotelOptional.get();
            hotel = modelMapper.map(hotelRequest , Hotel.class);
            Hotel newHotel = hotelRepository.save(hotel);
            hotelResponse = modelMapper.map(newHotel , HotelResponse.class);
        }
        return hotelResponse;
    }


}
