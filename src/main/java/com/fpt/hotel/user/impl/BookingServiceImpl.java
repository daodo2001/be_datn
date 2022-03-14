package com.fpt.hotel.user.impl;

import com.fpt.hotel.config.AppConts;
import com.fpt.hotel.model.*;
import com.fpt.hotel.repository.*;
import com.fpt.hotel.service.MailerService;
import com.fpt.hotel.user.dto.request.BookingRequest;
import com.fpt.hotel.user.dto.request.CheckInCheckOutRequest;
import com.fpt.hotel.user.dto.response.BookingResponse;
import com.fpt.hotel.user.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    TransactionInfoRepository transactionInfoRepository;

    @Autowired
    Booking_checkin_checkoutRepository bookingCheckinCheckoutRepository;

    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    HotelTypeRoomRepository hotelTypeRoomRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    MailerService mailerService;

    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public BookingResponse create(BookingRequest bookingRequest) throws MessagingException {
        bookingRequest.setStatus(AppConts.DEPOSITED);

        CheckInCheckOutRequest checkInCheckOutRequest = bookingRequest.getCheckInCheckOutRequest();
        Type_room typeRoom = typeRoomRepository.findById(checkInCheckOutRequest.getIdTypeRoom()).get();
        double tongTien = typeRoom.getPrice() * bookingRequest.getTongNgay() * bookingRequest.getTongSoPhong();

        bookingRequest.setTotalPrice(tongTien);
        bookingRequest.setDeposit_price(bookingRequest.getDeposit_price());

        Booking booking = modelMapper.map(bookingRequest, Booking.class);
        booking.setTotalRooms(bookingRequest.getTongSoPhong());
        booking.setCreate_date(new Date());
        Booking newBooking = bookingRepository.save(booking);

        checkInCheckOutRequest.setIdBooking(newBooking.getId());
        Booking_checkin_checkout booking_checkin_checkout =
                modelMapper.map(checkInCheckOutRequest, Booking_checkin_checkout.class);

        List<Booking_checkin_checkout> booking_checkin_checkouts = new ArrayList<>();
        booking_checkin_checkouts.add(booking_checkin_checkout);
        newBooking.setCheckinCheckouts(booking_checkin_checkouts);

        List<Booking_checkin_checkout> newBooking_checkin_checkouts = bookingCheckinCheckoutRepository.saveAll(booking_checkin_checkouts);
        Booking_checkin_checkout checkinCheckout = newBooking_checkin_checkouts.get(0);

        System.out.println(checkinCheckout.getTypeRoom().getName());
        if (newBooking.getId() != null) {
            Hotel hotel = hotelRepository.findById(booking.getId_hotel()).get();
            Type_room type_room = typeRoomRepository.findById(checkinCheckout.getTypeRoom().getId()).get();
            StringBuilder stringBuilder = new StringBuilder();
            String username = userRepository.findById(newBooking.getId_user().intValue()).get().getUsername();
            stringBuilder.append(
                    " <div>\n" +
                            "    <div style=\"background-color: darkcyan\">\n" +
                            "        <div><img src=\"https://i.ibb.co/PhbD4Bc/FPT-Polytechnic.png\" alt=\"\" width=\"250px\" style=\"padding: 20px;\" /></div>\n" +
                            "    </div>\n" +
                            "    <div style=\"margin-left: 30px;font-size: 20px;\">\n" +
                            "        <div style=\"margin-top: 20px;\">Xin chào<b> " + username + "!</b></div>\n" +
                            "        <div style=\"margin-top: 20px;\">Fpoly Cảm Ơn Bạn Đã Sử Dụng Dịch Vụ!</div>\n" +
                            "        <div style=\"margin-top: 20px;\">Thông tin đơn đặt của bạn!</b></div>\n" +
                            "        <ul>\n" +
                            "            <li>Mã đơn đặt:<b> " +newBooking.getId()+ "</b></li><br />\n" +
                            "            <li>Họ tên:<b> " +newBooking.getFull_name()+ "</b></li><br />\n" +
                            "            <li>Cơ sở:<b> " +  hotel.getName() +"</b></li><br />\n" +
                            "            <li>Ngày check-in: <b>" + checkinCheckout.getDate_in() + "</b></li><br />\n" +
                            "            <li>Ngày check-out:<b>" + checkinCheckout.getDate_out() + "</b></li><br />\n" +
                            "            <li>Tổng tiền: <b>" + booking.getTotalPrice() + " vnd</b></li><br />\n" +
                            "        </ul>\n" +
                            "       \n" +
                            "    </div>\n" +
                            "    <div style=\"background-color: darkcyan; height: 50px;\">\n" +
                            "        <br />\n" +
                            "        <i style=\"margin: 30px;color: aliceblue;\">Chúc bạn có trải nghiệm vui vẻ!</i>\n" +
                            "    </div>\n" +
                            "    </div>"
            );

            mailerService.queue(newBooking.getEmail(), "Xác nhận đặt phòng thành công", stringBuilder.toString());
        }

        return getBookingResponse(newBooking);

    }

    private BookingResponse getBookingResponse(Booking newBooking) {
        BookingResponse bookingResponse = new BookingResponse();

        modelMapper.map(newBooking, bookingResponse);
        return bookingResponse;
    }
}
