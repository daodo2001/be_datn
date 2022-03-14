package com.fpt.hotel.owner.controller;

import com.fpt.hotel.owner.dto.request.HotelTypeRoomRequest;
import com.fpt.hotel.owner.dto.response.HotelTypeRoomPublicResponse;
import com.fpt.hotel.owner.dto.response.HotelTypeRoomResponse;
import com.fpt.hotel.owner.service.IHotelTypeRoomService;
import com.fpt.hotel.payload.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/hotel-type-room")
@CrossOrigin("*")
public class HotelTypeRoomController {

    @Autowired
    IHotelTypeRoomService iHotelTypeRoomService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@RequestBody HotelTypeRoomRequest hotelTypeRoomRequest) {

        List<HotelTypeRoomResponse> hotelTypeRoomResponseList = iHotelTypeRoomService.checkHotelTypeRoom(hotelTypeRoomRequest);

        if(hotelTypeRoomResponseList.size() > 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Cập nhật số phòng thất bại ,"
                            + hotelTypeRoomResponseList.get(0).getNameHotel() +
                            " đã có loại phòng này!", hotelTypeRoomResponseList)
            );
        }

        HotelTypeRoomResponse hotelTypeRoomResponse =  iHotelTypeRoomService.save(hotelTypeRoomRequest);

        if (hotelTypeRoomResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Cập nhật số phòng thành công!", hotelTypeRoomResponse)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Cập nhật số phòng thất bại!", hotelTypeRoomResponse)
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll(@RequestParam(value = "idHotel" , required = false) Long idHotel) {

        List<HotelTypeRoomResponse> hotelTypeRoomResponses =  iHotelTypeRoomService.findAll(idHotel);
        if (!hotelTypeRoomResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Trả về danh sách số phòng thành công!", hotelTypeRoomResponses)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Trả về danh sách số phòng thất bại!", hotelTypeRoomResponses)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> hotelTypeRoomPublic(
            @PathVariable ("id") Long idHotel,
            @RequestParam (value = "id-type-room" , required = false) Long idTypeRoom,
            @RequestParam(value = "date-in" , required = false) String dateIn,
            @RequestParam(value = "date-out" , required = false) String dateOut
            ) {

        List<HotelTypeRoomPublicResponse> hotelTypeRoomResponses =
                iHotelTypeRoomService.hotelTypeRoomPublicResponses(idHotel , idTypeRoom  , dateOut , dateIn);
        if (!hotelTypeRoomResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Trả về danh sách số phòng thành công!", hotelTypeRoomResponses)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Trả về danh sách số phòng thất bại!", hotelTypeRoomResponses)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("")
    public ResponseEntity<ResponseObject> update(@RequestBody HotelTypeRoomRequest hotelTypeRoomRequest) {

        HotelTypeRoomResponse hotelTypeRoomResponse =  iHotelTypeRoomService.update(hotelTypeRoomRequest);

        if (hotelTypeRoomResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(), "Cập nhật số phòng thành công!", hotelTypeRoomResponse)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Cập nhật số phòng thất bại!", null)
        );
    }
}
