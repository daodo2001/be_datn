package com.fpt.hotel.owner.controller;

import com.fpt.hotel.owner.dto.response.*;
import com.fpt.hotel.owner.service.StatisticalService;
import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owner/thong-ke")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin("*")
public class StatisticalController {

    @Autowired
    StatisticalService statisticalService;

    @Autowired
    BookingRepository bookingRepository;

    @GetMapping("top-user-order")
    public ResponseEntity<ResponseObject> topUserOrder() {
        List<TopUserOrder> topUserOrderList = statisticalService.topUserOrderList();
        if (topUserOrderList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu!", topUserOrderList)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi top danh sách người dùng đặt phòng thành công!", topUserOrderList)
        );
    }

    @GetMapping("top-hotel-order")
    public ResponseEntity<ResponseObject> topHotelOrder() {
        List<TopHotelOrder> topHotelOrders = statisticalService.topHotelOrderList();
        if (topHotelOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu!", topHotelOrders)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi top danh sách cơ sở có nhiều người đặt phòng thành công!", topHotelOrders)
        );
    }

    @GetMapping("total-all")
    public ResponseEntity<ResponseObject> totalAll() {
        Total totalUsers = statisticalService.totalUsers();
        Total totalHotel = statisticalService.totalHotel();
        Total totalBookings = statisticalService.totalBookings();
        Total totalRevenue = statisticalService.totalDoanhThu();

        Map<String, Object> totalALl = new HashMap<>();
        totalALl.put("totalUsers", totalUsers);
        totalALl.put("totalHotel", totalHotel);
        totalALl.put("totalBookings", totalBookings);
        totalALl.put("totalRevenue", totalRevenue);
        if (totalALl.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu!", totalALl)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi dữ liệu thành công!", totalALl)
        );
    }

    @GetMapping("chart")
    public ResponseEntity<ResponseObject> chartResponse() {

        if (statisticalService.chartResponse().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu!", statisticalService.chartResponse())
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi dữ liệu thành công!", statisticalService.chartResponse())
        );
    }
    @GetMapping("column-chart")
    public ResponseEntity<ResponseObject> columnChart(@RequestParam("idHotel") Long idHotel) {

//        List<ChartResponse> chartResponses = statisticalService.chartResponse();
        List<Map<String, Object>> columnChart =  statisticalService.columnChart(idHotel);
        if (columnChart.get(0).get("data") == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Hiển thi dữ liệu thành công!", columnChart)
        );
    }
}
