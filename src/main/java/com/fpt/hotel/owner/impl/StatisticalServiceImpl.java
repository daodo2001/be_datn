package com.fpt.hotel.owner.impl;

import com.fpt.hotel.model.Hotel;
import com.fpt.hotel.owner.dto.response.*;
import com.fpt.hotel.owner.service.StatisticalService;
import com.fpt.hotel.repository.BookingRepository;
import com.fpt.hotel.repository.HotelRepository;
import com.fpt.hotel.repository.TransactionInfoRepository;
import com.fpt.hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticalServiceImpl implements StatisticalService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    TransactionInfoRepository transactionInfoRepository;

    @Override
    public List<TopUserOrder> topUserOrderList() {

        return bookingRepository.topUserOrder();
    }

    @Override
    public List<TopHotelOrder> topHotelOrderList() {
        return bookingRepository.topHotelOrder();
    }

    @Override
    public Total totalUsers() {
        return userRepository.totalUsers();
    }

    @Override
    public Total totalHotel() {
        return hotelRepository.totalHotel();
    }

    @Override
    public List chartResponse() {

        List pieData = new ArrayList<>();
        List dataElement = new ArrayList();
        List lableElement = new ArrayList();
        List<Hotel> hotels = hotelRepository.findAll();
        for (int i = 0; i < hotels.size(); i++) {
            Hotel hotel = hotels.get(i);
            ChartResponse chartResponse = transactionInfoRepository.chartResponse(hotel.getId());
            lableElement.add(chartResponse.getName());
            dataElement.add(chartResponse.getTotal());
        }
        pieData.add(lableElement);
        pieData.add(dataElement);
        return pieData;
    }

    @Override
    public Total totalDoanhThu() {
        return transactionInfoRepository.totalDoanhThu();
    }

    @Override
    public Total totalBookings() {
        return bookingRepository.totalBookings();
    }

    @Override
    public List<Map<String, Object>> columnChart(Long idHotel) {
        List<Map<String, Object>> columnData = new ArrayList<>();
        Map<String, Object> headerElement = new HashMap<>();
        headerElement.put("data", new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        columnData.add(headerElement);

        Map<String, Object> dataElement = new HashMap<>();
        Hotel hotel = hotelRepository.findById(idHotel).get();
        ColumnChart columnChart =  transactionInfoRepository.columchart(idHotel);
        dataElement.put("name", hotel.getName());
        if(columnChart.getJan() == null){
            dataElement.put("data" , null);
            return columnData;
        }
        dataElement.put("data", new Double[]{
                Double.valueOf(columnChart.getJan()),
                Double.valueOf(columnChart.getFeb()),
                Double.valueOf(columnChart.getMar()),
                Double.valueOf(columnChart.getApr()),
                Double.valueOf(columnChart.getMay()),
                Double.valueOf(columnChart.getJun()),
                Double.valueOf(columnChart.getJul()),
                Double.valueOf(columnChart.getAug()),
                Double.valueOf(columnChart.getSep()),
                Double.valueOf(columnChart.getOct()),
                Double.valueOf(columnChart.getNov()),
                Double.valueOf(columnChart.getDec())});
        columnData.add(dataElement);

        return columnData;
    }
}
