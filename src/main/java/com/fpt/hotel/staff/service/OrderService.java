package com.fpt.hotel.staff.service;

import com.fpt.hotel.staff.dto.request.OrderServiceRequest;
import com.fpt.hotel.staff.dto.response.BookingByCheckIn;
import com.fpt.hotel.staff.dto.response.OderServiceResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {

    List<BookingByCheckIn> listBookingByCheckIn(Integer idUser);

    Map<String , Object> getRoomAndGetService(Long idHotel , Long idTypeRoom);

    List<OderServiceResponse> save(OrderServiceRequest orderServiceRequest);
}
