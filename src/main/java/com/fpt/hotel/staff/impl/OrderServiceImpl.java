package com.fpt.hotel.staff.impl;

import com.fpt.hotel.model.Room;
import com.fpt.hotel.model.ServiceHotel;
import com.fpt.hotel.model.TransactionServiceHotel;
import com.fpt.hotel.model.Transaction_Info;
import com.fpt.hotel.repository.*;
import com.fpt.hotel.staff.dto.request.OrderServiceRequest;
import com.fpt.hotel.staff.dto.request.ServiceRequest;
import com.fpt.hotel.staff.dto.response.BookingByCheckIn;
import com.fpt.hotel.staff.dto.response.OderServiceResponse;
import com.fpt.hotel.staff.dto.response.RoomByUsingResponse;
import com.fpt.hotel.staff.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ServiceHotelRepository serviceHotelRepository;

    @Autowired
    TransactionInfoRepository transactionInfoRepository;

    @Autowired
    TransactionServiceHotelRepository transactionServiceHotelRepository;

    @Override
    public List<BookingByCheckIn> listBookingByCheckIn(Integer idUser) {
        return bookingRepository.listBookingByCheckIn(idUser);
    }

    @Override
    public Map<String, Object> getRoomAndGetService(Long idHotel, Long idTypeRoom) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        List<Room> roomsByUsing =  roomRepository.findAllByStatusUsing(idHotel, idTypeRoom);
        List<RoomByUsingResponse> roomByUsingResponses =
                roomsByUsing.stream().map(room ->
                        modelMapper.map(room , RoomByUsingResponse.class)).collect(Collectors.toList());
        List<ServiceHotel> serviceHotels = serviceHotelRepository.findAll();
        stringObjectMap.put("rooms" , roomByUsingResponses);
        stringObjectMap.put("service" , serviceHotels);
        return stringObjectMap;
    }

    @Override
    public List<OderServiceResponse> save(OrderServiceRequest orderServiceRequest) {
        Room room = roomRepository.findById(orderServiceRequest.getIdRoom()).get();
        Transaction_Info transaction_info = transactionInfoRepository.findByIdBooking(orderServiceRequest.getIdBooking());
        List<ServiceRequest> orderServices = orderServiceRequest.getServiceRequestList();
        List<TransactionServiceHotel> serviceHotels = new ArrayList<>();
        TransactionServiceHotel transactionServiceHotel = null;
        for (ServiceRequest serviceRequest:orderServices) {
            TransactionServiceHotel newTransactionServiceHotel = null;
            Optional<TransactionServiceHotel> transactionServiceHotelOptional = transactionServiceHotelRepository.findTransactionServiceHotel(
                    room.getId() ,serviceRequest.getIdService() ,transaction_info.getId());
            if(transactionServiceHotelOptional.isEmpty()){
                transactionServiceHotel = new TransactionServiceHotel();
                transactionServiceHotel.setIdServiceHotel(serviceRequest.getIdService());
                transactionServiceHotel.setIdRoom(room.getId());
                transactionServiceHotel.setIdTransaction(transaction_info.getId());
                transactionServiceHotel.setQuantity(serviceRequest.getQuantity());
                newTransactionServiceHotel = transactionServiceHotelRepository.save(transactionServiceHotel);
            }else {
                transactionServiceHotel = transactionServiceHotelOptional.get();
                transactionServiceHotel.setQuantity(transactionServiceHotel.getQuantity() + serviceRequest.getQuantity());
                newTransactionServiceHotel = transactionServiceHotelRepository.save(transactionServiceHotel);
            }

            serviceHotels.add(newTransactionServiceHotel);
        }
        return serviceHotels.stream().map(item ->
                modelMapper.map(item , OderServiceResponse.class)).collect(Collectors.toList());
    }


}
