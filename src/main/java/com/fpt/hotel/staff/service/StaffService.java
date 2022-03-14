package com.fpt.hotel.staff.service;

import com.fpt.hotel.staff.dto.request.*;
import com.fpt.hotel.staff.dto.response.*;

import java.util.List;
import java.util.Map;

public interface StaffService {

    TransactionInfoResponse confirmBooking(TransactionRequest transactionRequest );

    List<ManagerOrderResponse> listOrderResponses(Integer idUser , String status);

    StaffByHotel staffByHotel(Integer idUser);

    CancelRoom cancelRoom(RefundVnpayRequest refundVnpayRequest);

    GiaHanPhongResponse giaHanPhong(GiaHanPhongRequest giaHanPhongRequest);

    Integer checkTotalRoom(Long idHotel , Long idTypeRoom, String dateIn , String dateOut);

    UpdateBookingResponse updateBooking(UpdateBookingRequest updateBookingRequest);

    Map<String , Object> getRoomAndGetUtility(Long idHotel , Long idTypeRoom);

    List<OrderDetailsResponse> orderDetailsResponses(Long idTransaction);

    Long staffBooking(StaffBookingRequest staffBookingRequest);
 }
