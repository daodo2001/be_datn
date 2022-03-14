package com.fpt.hotel.owner.service;

import com.fpt.hotel.model.User;
import com.fpt.hotel.owner.dto.request.UserRequest;
import com.fpt.hotel.owner.dto.response.OwnerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OwnerService {

    List<OwnerResponse> findAll(String roleName);

    OwnerResponse save(String folder, String user, List<MultipartFile>  files);

    OwnerResponse updateIsEnabled(Integer id);

    OwnerResponse updateHotel(Integer idUser , Long idHotel);

    OwnerResponse findById(Integer id);

    OwnerResponse updateUser(String user , List<MultipartFile> files);

    OwnerResponse changePassword(Integer idUser , String oldPassword , String newPassword);

}
