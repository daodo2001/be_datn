package com.fpt.hotel.owner.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.hotel.model.TypeRoomImage;
import com.fpt.hotel.model.Type_room;
import com.fpt.hotel.owner.dto.response.TypeRoomResponse;
import com.fpt.hotel.owner.service.ITypeRoomService;
import com.fpt.hotel.repository.TypeRoomImageRepository;
import com.fpt.hotel.repository.TypeRoomRepository;
import com.fpt.hotel.service.FileManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeRoomServiceImpl implements ITypeRoomService {

    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    TypeRoomImageRepository typeRoomImageRepository;

    @Autowired
    FileManagerService fileManagerService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public TypeRoomResponse save(String folder, String typeRoom, List<MultipartFile> files) throws JsonProcessingException {

        Type_room typeRoomSave = getTypeRoom(typeRoom);

        List<TypeRoomImage> typeRoomImages = getTypeRoomImages(folder, files, typeRoomSave);

        // thêm TypeRoomImage vào list để thêm vào loại phòng

        typeRoomSave.setTypeRoomImages(typeRoomImages);

        // cập nhật lại loại phòng khi có đủ các trường trong TypeRoomImage
        Type_room type_room = typeRoomRepository.save(typeRoomSave);
        return modelMapper.map(type_room, TypeRoomResponse.class );

    }

    @Override
    public List<TypeRoomResponse> findAll() {
        return typeRoomRepository.findAll().stream().map(item -> modelMapper.map(item, TypeRoomResponse.class
        )).collect(Collectors.toList());
    }

    @Override
    public TypeRoomResponse update(String folder, String typeRoom, List<MultipartFile> files) throws JsonProcessingException {
        Type_room typeRoomSave = getTypeRoom(typeRoom);

        List<TypeRoomImage> typeRoomImages = getTypeRoomImages(folder, files, typeRoomSave);
        if(!typeRoomImages.isEmpty()){
            typeRoomSave.setTypeRoomImages(typeRoomImages);
        }
        Type_room type_room = typeRoomRepository.save(typeRoomSave);
        return modelMapper.map(type_room, TypeRoomResponse.class );
    }

    // lưu typeRoomImage
    private List<TypeRoomImage> getTypeRoomImages(String folder, List<MultipartFile> files, Type_room typeRoomSave) {
        // tạo đối tượng TypeRoomImage
        List<TypeRoomImage> typeRoomImages = new ArrayList<>();
        TypeRoomImage typeRoomImage = null;

        if (files != null) {
            List<String> listFile = fileManagerService.save(folder, files);
            for (String file : listFile) {
                typeRoomImage = new TypeRoomImage();
                typeRoomImage.setTypeRoom(typeRoomSave);
                typeRoomImage.setImage(file);
                typeRoomImages.add(typeRoomImage);
            }
        }

        return typeRoomImageRepository.saveAll(typeRoomImages);

    }

    // convert obj sang TypeRoom và lưu vào db
    private Type_room getTypeRoom(String typeRoom) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Type_room typeRoomJson = objectMapper.readValue(typeRoom, Type_room.class);

        return typeRoomRepository.save(typeRoomJson);
    }
}
