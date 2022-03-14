package com.fpt.hotel.owner.service;

import com.fpt.hotel.owner.dto.response.ColumnChart;
import com.fpt.hotel.owner.dto.response.TopHotelOrder;
import com.fpt.hotel.owner.dto.response.TopUserOrder;
import com.fpt.hotel.owner.dto.response.Total;

import java.util.List;
import java.util.Map;

public interface StatisticalService {

    List<TopUserOrder> topUserOrderList();

    List<TopHotelOrder> topHotelOrderList();

    Total totalUsers();

    Total totalHotel();

    List chartResponse();

    Total totalDoanhThu();

    Total totalBookings();

    List<Map<String, Object>> columnChart(Long idHotel);
}
