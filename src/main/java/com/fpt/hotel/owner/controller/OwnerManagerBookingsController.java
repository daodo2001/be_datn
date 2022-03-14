package com.fpt.hotel.owner.controller;


import com.fpt.hotel.owner.dto.response.ManageOrderBookingsResponse;
import com.fpt.hotel.owner.dto.response.OwnerResponse;
import com.fpt.hotel.owner.service.ManageOrderService;
import com.fpt.hotel.payload.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/manage-bookings")
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
public class OwnerManagerBookingsController {

    @Autowired
    ManageOrderService manageOrderService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> listCheckIn(@RequestParam("status") String status ,
                                                 @RequestParam(value = "idHotel" , required = false) Long idHotel) {

        List<ManageOrderBookingsResponse> listBookings = manageOrderService.listBookings(status , idHotel);
        if (listBookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Không có đơn đặt nào", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Trả về dữ liệu đơn đặt thành công", listBookings));
    }

}
