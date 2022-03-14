package com.fpt.hotel.staff.controller;

import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.staff.dto.request.OrderServiceRequest;
import com.fpt.hotel.staff.dto.response.BookingByCheckIn;
import com.fpt.hotel.staff.dto.response.OderServiceResponse;
import com.fpt.hotel.staff.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/staff/order-service")
@CrossOrigin("*")
public class StaffOrderServiceController {

    @Autowired
    OrderService orderService;

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> bookingByStaff(@PathVariable("id") Integer idUser) {
        List<BookingByCheckIn> bookingByCheckIns = orderService.listBookingByCheckIn(idUser);
        if (bookingByCheckIns.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có dữ liệu!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Trả về danh sách đã nhận phòng thành công!", bookingByCheckIns)
        );
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getRoomsAndService(
            @RequestParam("idHotel") Long idHotel,
            @RequestParam("idTypeRoom") Long idTypeRoom) {
        Map<String , Object> roomsAndServiceResponse = orderService.getRoomAndGetService(idHotel , idTypeRoom);
        if (roomsAndServiceResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có dữ liệu!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Trả về dữ liệu thành công!", roomsAndServiceResponse)
        );
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> saveOrderService(
            @RequestBody OrderServiceRequest orderServiceRequest) {
        List<OderServiceResponse> save = orderService.save(orderServiceRequest);
        if (save.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có dữ liệu!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Đặt dịch vụ theo phòng thành công!", save)
        );
    }
}
