package com.fpt.hotel.owner.controller;

import com.fpt.hotel.model.RoomUtility;
import com.fpt.hotel.owner.dto.request.TypeRoomUtilityRequest;
import com.fpt.hotel.owner.dto.response.RoomUtilityResponse;
import com.fpt.hotel.owner.dto.response.UtilityResponse;
import com.fpt.hotel.owner.service.RoomUtilityService;
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
@RequestMapping("/api/owner/utilities")
@PreAuthorize("hasRole('ADMIN')")
public class UtitlityController {

    @Autowired
    UtitlityService utitlityService;

    @Autowired
    RoomUtilityService roomUtilityService;

    @GetMapping
    public ResponseEntity<ResponseObject> findAllHotels() {
        List<UtilityResponse> responseList = utitlityService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi tất cả các tiện ích thành công!", responseList)
        );
    }

    @PostMapping(value = "/{folder}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> create(@PathVariable("folder") String folder, @RequestPart("utility") String utility,
                                                      @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        UtilityResponse utilityResponse = utitlityService.save(folder, utility, files);
        if (utilityResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Tạo mới tiện ích thành công!", utilityResponse)
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Đã có tên tiện ích này!", null)
        );
    }

    @PutMapping(value = "/{folder}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> update(@PathVariable("folder") String folder, @RequestPart("utility") String utility,
                                                      @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        UtilityResponse utilityResponse = utitlityService.update(folder, utility, files);
        if (utilityResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Cập nhật tiện ích thành công!", utilityResponse)
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Đã có tên tiện ích này!", null)
        );
    }

    @PostMapping("type-room-utility")
    public ResponseEntity<ResponseObject> saveTypeRoomUtitiliy(@RequestBody TypeRoomUtilityRequest typeRoomUtilityRequest) {

        List<RoomUtilityResponse> typeRoomUtitiliy = roomUtilityService.saveTypeRoomUtitiliy(typeRoomUtilityRequest);
        if (!typeRoomUtitiliy.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(
                            HttpStatus.OK.toString(),
                            "Thêm tiện ích theo loại phòng thành công!",
                            typeRoomUtitiliy)
            );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Loại phòng này đã có những tiện ích này!", null)
        );
    }
}
