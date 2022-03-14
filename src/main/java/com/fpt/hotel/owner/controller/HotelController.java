package com.fpt.hotel.owner.controller;


import com.fpt.hotel.model.Hotel;
import com.fpt.hotel.owner.dto.request.HotelRequest;
import com.fpt.hotel.owner.dto.response.HotelResponse;
import com.fpt.hotel.owner.impl.HotelServiceImpl;
import com.fpt.hotel.owner.service.IHotelService;
import com.fpt.hotel.payload.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/owner/hotels")
public class HotelController {

    @Autowired
    private IHotelService hotelService;

    @GetMapping
    public ResponseEntity<ResponseObject> findAllHotels() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi tất cả các cơ sở!", hotelService.findAllHotels())
        );
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> findByHotel(@PathVariable("id") Long id) {
        HotelResponse hotelResponse = hotelService.findById(id);
        if (hotelResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có cơ sở này!", hotelResponse)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Trả về cơ sở thành công!", hotelResponse)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{folder}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> createHotel(@PathVariable("folder") String folder, @RequestPart("hotel") String hotel,
                                                      @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        HotelResponse newHotel = hotelService.createHotel(folder, hotel, files);
        if (newHotel != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Tạo mới cơ sở hotel thành công!", newHotel)
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Đã có tên cơ sở này!", newHotel)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "{folder}/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> updateHotel(@PathVariable("id") Long id, @PathVariable("folder") String folder, @RequestPart("hotel") String hotel,
                                                      @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        HotelResponse newHotel = hotelService.updateHotel(id, folder, hotel, files);
        if (newHotel != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Cập nhật cơ sở thành công!", newHotel)
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Cập nhật cơ sở thất bại!", newHotel)
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseObject> updateIsEnabled(@PathVariable("id") Long id) {

        HotelResponse hotelResponse = hotelService.updateIsEnabled(id);
        if (hotelResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Cập nhật cơ sở thành công!", hotelResponse)
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Cập nhật cơ sở thất bại!", hotelResponse)
        );
    }

    @PutMapping("")
    public ResponseEntity<ResponseObject> updateHotel(@RequestBody HotelRequest hotelRequest) {

        HotelResponse hotelResponse = hotelService.updateHotel(hotelRequest);
        if (hotelResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Cập nhật cơ sở thành công!", hotelResponse)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Cập nhật cơ sở thất bại!", hotelResponse)
        );
    }


}
