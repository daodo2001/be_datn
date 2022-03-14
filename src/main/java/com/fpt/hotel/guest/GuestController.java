package com.fpt.hotel.guest;

import com.fpt.hotel.guest.response.GuestBookingResponse;
import com.fpt.hotel.model.Booking;
import com.fpt.hotel.owner.dto.response.HotelResponse;
import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/guest/code-bookings")
public class GuestController {

    @Autowired
    BookingRepository bookingRepository;

    @PostMapping("")
    public ResponseEntity<ResponseObject> searchBooking(@RequestBody Long idBooking) {

        Optional<GuestBookingResponse> optionalBooking = bookingRepository.guestBookingResponse(idBooking);
        if (optionalBooking.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Đã tìm kiếm thấy mã đơn đặt!", optionalBooking.get())
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có mã đơn đặt này", null)
        );
    }

}
