package com.fpt.hotel.owner.impl;

import com.fpt.hotel.model.Booking;
import com.fpt.hotel.owner.dto.response.ManageOrderBookingsResponse;
import com.fpt.hotel.owner.service.ManageOrderService;
import com.fpt.hotel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManageOrderServiceImpl implements ManageOrderService {

    @Autowired
    BookingRepository bookingRepository;

    @Override
    public List<ManageOrderBookingsResponse> listBookings(String status , Long idHotel) {
        return this.bookings(status , idHotel);
    }
    private List<ManageOrderBookingsResponse> bookings(String status , Long idHotel){
        List<ManageOrderBookingsResponse> bookings = new ArrayList<>();
        if(idHotel != null){
            bookings = bookingRepository.bookingsByStatusAndIdHotel(status, idHotel);
        }else {
            bookings = bookingRepository.bookingsByStatusAndIdHotel(status);
        }
        return bookings;
    }
}
