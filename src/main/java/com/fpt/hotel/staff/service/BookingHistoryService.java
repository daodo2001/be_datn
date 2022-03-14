package com.fpt.hotel.staff.service;

import com.fpt.hotel.staff.dto.response.BookingHistoryResponse;

import java.util.List;

public interface BookingHistoryService {

    List<BookingHistoryResponse> bookingHistoryResponses(Integer idChanger);

    List<BookingHistoryResponse> findAllByHotel(Long idHotel);
}
