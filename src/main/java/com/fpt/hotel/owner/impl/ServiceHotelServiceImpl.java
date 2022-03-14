package com.fpt.hotel.owner.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.hotel.model.ServiceHotel;
import com.fpt.hotel.model.Utility;
import com.fpt.hotel.owner.dto.response.ServiceHotelResponse;
import com.fpt.hotel.owner.dto.response.UtilityResponse;
import com.fpt.hotel.owner.service.ServiceHotelService;
import com.fpt.hotel.repository.ServiceHotelRepository;
import com.fpt.hotel.service.FileManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceHotelServiceImpl implements ServiceHotelService {

    @Autowired
    ServiceHotelRepository serviceHotelRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileManagerService fileManagerService;

    @Override
    public List<ServiceHotelResponse> findAll() {
        return serviceHotelRepository.findAll().stream().map(
                serviceHotel -> modelMapper.map(serviceHotel , ServiceHotelResponse.class)
        ).collect(Collectors.toList());
    }

    @Override
    public ServiceHotelResponse save(String folder, String serviceHotel, List<MultipartFile> files) {
        ServiceHotel serviceHotelJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            serviceHotelJson = objectMapper.readValue(serviceHotel, ServiceHotel.class);

            String fileName = "";
            Boolean existsByName = serviceHotelRepository.existsByName(serviceHotelJson.getName());

            if (existsByName) {
                return null;
            }
            if (files != null) {
                List<String> fileLists = fileManagerService.save(folder, files);
                fileName = fileLists.get(0);
            }

            serviceHotelJson.setImage(fileName);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        ServiceHotel newServiceHotel =  serviceHotelRepository.save(serviceHotelJson);
        return modelMapper.map(newServiceHotel , ServiceHotelResponse.class);
    }

    @Override
    public ServiceHotelResponse update(String folder, String serviceHotel, List<MultipartFile> files) {
        ServiceHotel serviceHotelJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            serviceHotelJson = objectMapper.readValue(serviceHotel, ServiceHotel.class);

            String fileName = "";

            if (files != null) {
                List<String> fileLists = fileManagerService.save(folder, files);
                fileName = fileLists.get(0);
            }
            else {
                fileName = serviceHotelJson.getImage();
            }

            serviceHotelJson.setImage(fileName);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        ServiceHotel newServiceHotel =  serviceHotelRepository.save(serviceHotelJson);
        return modelMapper.map(newServiceHotel , ServiceHotelResponse.class);
    }
}
