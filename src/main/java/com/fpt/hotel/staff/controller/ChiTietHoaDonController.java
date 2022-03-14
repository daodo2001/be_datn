package com.fpt.hotel.staff.controller;

import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.staff.dto.response.ChiTietHoaDonResponse;

import com.fpt.hotel.staff.service.ChiTietHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@PreAuthorize("hasRole('STAFF')")
@RequestMapping("api/staff")
@CrossOrigin("*")
public class ChiTietHoaDonController {

    @Autowired
    ChiTietHoaDonService chiTietHoaDonService;

    @GetMapping("chi-tiet-hoa-don")
    public ResponseEntity<ResponseObject> chiTietHoaDon(@RequestParam("idBooking") Long idBooking,
                                                       @RequestParam("idUser") Integer idUser) {
        ChiTietHoaDonResponse chiTietHoaDonResponse = chiTietHoaDonService.chiTietHoaDon(idBooking, idUser);
        if (chiTietHoaDonResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.name(), "Trả về dữ liệu người dùng đặt phòng thành công", chiTietHoaDonResponse));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu", null));
    }

}
