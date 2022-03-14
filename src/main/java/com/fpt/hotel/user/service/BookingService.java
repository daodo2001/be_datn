package com.fpt.hotel.user.service;

import com.fpt.hotel.model.Booking;
import com.fpt.hotel.user.dto.request.BookingRequest;
import com.fpt.hotel.user.dto.response.BookingResponse;

import javax.mail.MessagingException;


public interface BookingService {

    BookingResponse create(BookingRequest booking) throws MessagingException;
}
