package com.fpt.hotel.staff.impl;

import com.fpt.hotel.model.Booking;
import com.fpt.hotel.model.BookingHistory;
import com.fpt.hotel.model.Booking_checkin_checkout;
import com.fpt.hotel.repository.*;
import com.fpt.hotel.staff.dto.response.BookingHistoryResponse;
import com.fpt.hotel.staff.service.BookingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingHistoryServiceImpl implements BookingHistoryService {
    @Autowired
    BookingHistoryRepository bookingHistoryRepository;

    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelRepository hotelRepository;


    @Override
    public List<BookingHistoryResponse> bookingHistoryResponses(Integer idChanger) {
        List<BookingHistoryResponse> findAll = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        List<BookingHistory> bookingHistories = bookingHistoryRepository.findAll(idChanger);
        return getBookingHistoryResponses(findAll, formatter, sdf2, bookingHistories);
    }

    @Override
    public List<BookingHistoryResponse> findAllByHotel(Long idHotel) {
        List<BookingHistoryResponse> findAll = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<BookingHistory> bookingHistories = new ArrayList<>();
        if(idHotel != null){
            bookingHistories = bookingHistoryRepository.findAll(idHotel);
        }else {
            bookingHistories = bookingHistoryRepository.findAll();
        }

        return getBookingHistoryResponses(findAll, formatter, sdf2, bookingHistories);
    }

    private List<BookingHistoryResponse> getBookingHistoryResponses(List<BookingHistoryResponse> findAll, SimpleDateFormat formatter, SimpleDateFormat sdf2, List<BookingHistory> bookingHistories) {
        for (BookingHistory bookingHistory : bookingHistories) {
            BookingHistoryResponse bookingHistoryResponse = new BookingHistoryResponse();
            Booking booking = bookingRepository.findById(bookingHistory.getIdBooking()).get();
            Booking_checkin_checkout checkinCheckout = booking.getCheckinCheckouts().get(0);
            String strDateIn = formatter.format(bookingHistory.getDateIn());
            String strDateOut = formatter.format(bookingHistory.getDateOut());
            String strNewDateIn = formatter.format(checkinCheckout.getDate_in());
            String strNewDateOut = formatter.format(checkinCheckout.getDate_out());
            // hiển thị lịch cũ
            bookingHistoryResponse.setFullName(bookingHistory.getFullName());
            bookingHistoryResponse.setPhone(bookingHistory.getPhone());
            bookingHistoryResponse.setNameTypeRoom(bookingHistory.getNameTypeRoom());
            bookingHistoryResponse.setTotalRoom(bookingHistory.getTotalRoom());
            bookingHistoryResponse.setDateIn(strDateIn);
            bookingHistoryResponse.setDateOut(strDateOut);
            bookingHistoryResponse.setStatus(bookingHistory.getStatus());

            String nameHotel = hotelRepository.findById(booking.getId_hotel()).get().getName();
            bookingHistoryResponse.setNameHotel(nameHotel);
            // hiển thị lịch mới
            String ngayTao = sdf2.format(bookingHistory.getCreateDate());
            bookingHistoryResponse.setNewDateIn(strNewDateIn);
            bookingHistoryResponse.setNewDateOut(strNewDateOut);
            bookingHistoryResponse.setNewNameTypeRoom(checkinCheckout.getTypeRoom().getName());
            bookingHistoryResponse.setNewTotalRoom(booking.getTotalRooms());
            bookingHistoryResponse.setUsername(userRepository.findById(bookingHistory.getIdChanger()).get().getUsername());
            bookingHistoryResponse.setCreateDate(ngayTao);
            findAll.add(bookingHistoryResponse);
        }
        return findAll;
    }
}
