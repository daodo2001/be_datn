package com.fpt.hotel.staff.service;

import com.fpt.hotel.staff.dto.response.ChiTietHoaDonResponse;

public interface ChiTietHoaDonService {

    ChiTietHoaDonResponse chiTietHoaDon(Long idBooking , Integer idUser);
}
