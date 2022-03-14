package com.fpt.hotel.owner.service;

import com.fpt.hotel.owner.dto.response.ManageOrderBookingsResponse;

import java.util.List;

public interface ManageOrderService {

    List<ManageOrderBookingsResponse> listBookings(String status , Long idHotel);

}
