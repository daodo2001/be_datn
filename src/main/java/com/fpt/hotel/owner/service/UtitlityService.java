package com.fpt.hotel.owner.service;


import com.fpt.hotel.owner.dto.response.UtilityResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UtitlityService {

    List<UtilityResponse> findAll();

    UtilityResponse save(String folder, String utility, List<MultipartFile> files);

    UtilityResponse update(String folder, String utility, List<MultipartFile> files);
}
