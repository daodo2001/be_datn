package com.fpt.hotel.user.service;

import com.fpt.hotel.user.dto.response.BookingOrderResponse;
import com.fpt.hotel.user.dto.response.VnpayTransactionResponse;

import java.util.List;

public interface UserService {


    List<BookingOrderResponse> listBookingOrderResponses(Integer id);

    VnpayTransactionResponse findByIdBooking(Long idBooking);

}
