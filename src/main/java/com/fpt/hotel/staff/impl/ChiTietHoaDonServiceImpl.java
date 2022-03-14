package com.fpt.hotel.staff.impl;

import com.fpt.hotel.repository.BookingRepository;
import com.fpt.hotel.staff.dto.response.ChiTietHoaDonResponse;
import com.fpt.hotel.staff.service.ChiTietHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChiTietHoaDonServiceImpl implements ChiTietHoaDonService {
    @Autowired
    BookingRepository bookingRepository;

    @Override
    public ChiTietHoaDonResponse chiTietHoaDon(Long idBooking, Integer idUser) {
        return bookingRepository.chiTietHoaDon(idBooking , idUser);
    }
}
