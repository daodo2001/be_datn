package com.fpt.hotel.user.controller;

import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.user.dto.response.BookingOrderResponse;
import com.fpt.hotel.user.dto.response.UserResponse;
import com.fpt.hotel.user.dto.response.VnpayTransactionResponse;
import com.fpt.hotel.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()")
@CrossOrigin("*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("thong-tin-dat-phong")
    public ResponseEntity<ResponseObject> bookingOrder(@RequestParam("id") Integer id) {

        List<BookingOrderResponse> listBookingOrderResponses = userService.listBookingOrderResponses(id);

        if (listBookingOrderResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu", listBookingOrderResponses));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Trả về dữ liệu thông tin cá nhân thành công", listBookingOrderResponses));
    }

    @GetMapping("chi-tiet-dat-phong")
    public ResponseEntity<ResponseObject> bookingOrder(@RequestParam("id") Long id) {

        VnpayTransactionResponse vnpayTransactionResponse = userService.findByIdBooking(id);

        if (vnpayTransactionResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu", vnpayTransactionResponse));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK.name(), "Trả về dữ liệu thông tin chi tiết đặt phòng thành công" +
                        "", vnpayTransactionResponse));
    }

}
