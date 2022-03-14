package com.fpt.hotel.staff.controller;

import com.fpt.hotel.staff.dto.response.BookingHistoryResponse;
import com.fpt.hotel.staff.service.BookingHistoryService;
import com.fpt.hotel.payload.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/staff/booking-history")
@CrossOrigin("*")
@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
public class BookingHistoryController {

    @Autowired
    BookingHistoryService bookingHistoryService;

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> bookingHistoryByStaff(@PathVariable("id") Integer id) {
        List<BookingHistoryResponse> responseList =  bookingHistoryService.bookingHistoryResponses(id);
        if (responseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có dữ liệu!", responseList)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Trả về danh sách lịch sử đơn đặt thành công!", responseList)
        );
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll(@RequestParam(value = "idHotel" , required = false) Long idHotel) {
        List<BookingHistoryResponse> responseList =  bookingHistoryService.findAllByHotel(idHotel);
        if (responseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Không có dữ liệu!", responseList)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Trả về danh sách lịch sử đơn đặt thành công!", responseList)
        );
    }

}
