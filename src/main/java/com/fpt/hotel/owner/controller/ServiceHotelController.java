package com.fpt.hotel.owner.controller;

import com.fpt.hotel.owner.dto.response.ServiceHotelResponse;
import com.fpt.hotel.owner.dto.response.UtilityResponse;
import com.fpt.hotel.owner.service.ServiceHotelService;
import com.fpt.hotel.owner.service.UtitlityService;
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
@RequestMapping("/api/owner/service-hotel")
@PreAuthorize("hasRole('ADMIN')")
public class ServiceHotelController {

    @Autowired
    ServiceHotelService serviceHotelService;

    @GetMapping
    public ResponseEntity<ResponseObject> findAll() {
        List<ServiceHotelResponse> responseList = serviceHotelService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi tất cả các dịch vụ thành công!", responseList)
        );
    }

    @PostMapping(value = "/{folder}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> create(@PathVariable("folder") String folder, @RequestPart("service") String service,
                                                      @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        ServiceHotelResponse serviceHotelResponse = serviceHotelService.save(folder, service, files);
        if (serviceHotelResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Tạo mới dịch vụ thành công!", serviceHotelResponse)
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Đã có tên dịch vụ này!", null)
        );
    }

    @PutMapping(value = "/{folder}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> update(@PathVariable("folder") String folder, @RequestPart("service") String service,
                                                      @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        ServiceHotelResponse serviceHotelResponse = serviceHotelService.update(folder, service, files);
        if (serviceHotelResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Cập nhật dịch vụ thành công!", serviceHotelResponse)
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Đã có tên dịch vụ này!", null)
        );
    }
}
