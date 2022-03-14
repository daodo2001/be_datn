package com.fpt.hotel.user.impl;

import com.fpt.hotel.model.*;
import com.fpt.hotel.repository.BookingRepository;
import com.fpt.hotel.repository.HotelRepository;
import com.fpt.hotel.repository.UserRepository;
import com.fpt.hotel.repository.VnpayTransactionRepository;
import com.fpt.hotel.user.dto.response.BookingOrderResponse;
import com.fpt.hotel.user.dto.response.UserResponse;
import com.fpt.hotel.user.dto.response.VnpayTransactionResponse;
import com.fpt.hotel.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    VnpayTransactionRepository vnpayTransactionRepository;

    @Override
    public List<BookingOrderResponse> listBookingOrderResponses(Integer id) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        List<Booking> bookings =  bookingRepository.BookingOrderResponseList(id);
        List<BookingOrderResponse> bookingOrderResponseList = new ArrayList<>();

        for (Booking booking : bookings ) {
            BookingOrderResponse bookingOrderResponse = new BookingOrderResponse();
            Hotel hotel = hotelRepository.findById(booking.getId_hotel()).get();
            bookingOrderResponse.setIdBooking(booking.getId());
            bookingOrderResponse.setNameHotel(hotel.getName());
            List<Booking_checkin_checkout> checkinCheckouts =  booking.getCheckinCheckouts();
            for (Booking_checkin_checkout checkin_checkout: checkinCheckouts) {
                String strDateIn = formatter.format(checkin_checkout.getDate_in());
                String strDateOut = formatter.format(checkin_checkout.getDate_out());
                bookingOrderResponse.setDateIn(strDateIn);
                bookingOrderResponse.setDateOut(strDateOut);
                bookingOrderResponse.setNameTypeRoom(checkin_checkout.getTypeRoom().getName());
            }
            bookingOrderResponse.setTotalRoom(booking.getTotalRooms());
            bookingOrderResponse.setPrice(booking.getTotalPrice());
            bookingOrderResponse.setStatus(booking.getStatus());
            bookingOrderResponseList.add(bookingOrderResponse);
        }

        return bookingOrderResponseList;
    }

    @Override
    public VnpayTransactionResponse findByIdBooking(Long idBooking) {
        Optional<VnpayTransaction> optionalVnpayTransaction  =
                vnpayTransactionRepository.findByIdbooking(idBooking);
        if(optionalVnpayTransaction.isPresent()){
            return modelMapper.map(optionalVnpayTransaction.get(),
                    VnpayTransactionResponse.class);
        }
        return null;
    }
}
