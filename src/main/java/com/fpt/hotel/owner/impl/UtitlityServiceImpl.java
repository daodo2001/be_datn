package com.fpt.hotel.owner.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.hotel.model.Utility;
import com.fpt.hotel.owner.dto.response.UtilityResponse;
import com.fpt.hotel.owner.service.UtitlityService;
import com.fpt.hotel.repository.UtitlityRepository;
import com.fpt.hotel.service.FileManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtitlityServiceImpl implements UtitlityService {

    @Autowired
    UtitlityRepository utitlityRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileManagerService fileManagerService;

    @Override
    public List<UtilityResponse> findAll() {
        return utitlityRepository.findAll().stream().map(
                utility -> modelMapper.map(utility , UtilityResponse.class)
        ).collect(Collectors.toList());
    }

    @Override
    public UtilityResponse save(String folder, String utility, List<MultipartFile> files) {
        Utility utilityJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            utilityJson = objectMapper.readValue(utility, Utility.class);

            String fileName = "";
            Boolean existsByName = utitlityRepository.existsByName(utilityJson.getName());

            if (existsByName) {
                return null;
            }
            if (files != null) {
                List<String> fileLists = fileManagerService.save(folder, files);
                fileName = fileLists.get(0);
            }

            utilityJson.setImage(fileName);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        Utility newUtility =  utitlityRepository.save(utilityJson);
        return modelMapper.map(newUtility , UtilityResponse.class);
    }

    @Override
    public UtilityResponse update(String folder, String utility, List<MultipartFile> files) {
        Utility utilityJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            utilityJson = objectMapper.readValue(utility, Utility.class);

            String fileName = "";

            if (files != null) {
                List<String> fileLists = fileManagerService.save(folder, files);
                fileName = fileLists.get(0);
            }
            else {
                fileName = utilityJson.getImage();
            }

            utilityJson.setImage(fileName);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        Utility newUtility =  utitlityRepository.save(utilityJson);
        return modelMapper.map(newUtility , UtilityResponse.class);
    }
}
