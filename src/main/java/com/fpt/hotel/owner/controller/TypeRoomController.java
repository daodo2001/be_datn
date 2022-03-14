package com.fpt.hotel.owner.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.hotel.model.Type_room;
import com.fpt.hotel.owner.dto.response.TypeRoomResponse;
import com.fpt.hotel.owner.impl.TypeRoomServiceImpl;
import com.fpt.hotel.owner.service.ITypeRoomService;
import com.fpt.hotel.payload.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/owner/typerooms")
@CrossOrigin("*")
public class TypeRoomController {

    @Autowired
    ITypeRoomService typeRoomService;

    @GetMapping
    public ResponseEntity<ResponseObject> findAllHotels() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi tất cả các loại phòng thành công!", typeRoomService.findAll())
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "{folder}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> create(@PathVariable("folder") String folder, @RequestPart("typeRoom") String typeRoom,
                                                 @RequestPart(name = "file", required = false) List<MultipartFile> files) throws JsonProcessingException {

        TypeRoomResponse typeRoomResp = typeRoomService.save(folder, typeRoom, files);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Ok", "Thêm loại phòng thành công", typeRoomResp));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "{folder}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> update(@PathVariable("folder") String folder, @RequestPart("typeRoom") String typeRoom,
                                                 @RequestPart(name = "file", required = false) List<MultipartFile> files) throws JsonProcessingException {

        TypeRoomResponse typeRoomResp = typeRoomService.update(folder, typeRoom, files);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Ok", "Cập nhật loại phòng thành công", typeRoomResp));
    }
}
