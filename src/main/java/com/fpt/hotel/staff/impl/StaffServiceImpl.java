package com.fpt.hotel.staff.impl;

import com.fpt.hotel.config.AppConts;
import com.fpt.hotel.config.ConfigVnPay;
import com.fpt.hotel.model.*;
import com.fpt.hotel.owner.dto.response.UtilityResponse;
import com.fpt.hotel.repository.*;
import com.fpt.hotel.staff.dto.request.*;
import com.fpt.hotel.staff.dto.response.*;
import com.fpt.hotel.staff.service.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    VnpayTransactionRepository vnpayTransactionRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionInfoRepository transactionInfoRepository;

    @Autowired
    HotelTypeRoomRepository hotelTypeRoomRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    Booking_checkin_checkoutRepository bookingCheckinCheckoutRepository;

    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    BookingHistoryRepository bookingHistoryRepository;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UtitlityRepository utitlityRepository;

    @Autowired
    CostsIncurredRepository costsIncurredRepository;

    @Override
    @Transactional
    public TransactionInfoResponse confirmBooking(TransactionRequest transactionRequest) {
        String statusRequest = transactionRequest.getStatus();
        String newStatus = statusRequest.equalsIgnoreCase(AppConts.DEPOSITED) ? AppConts.CHECK_IN : AppConts.CHECK_OUT;

        Booking booking = bookingRepository.findById(transactionRequest.getIdBooking()).get();
        booking.setStatus(newStatus);
        if (newStatus == AppConts.CHECK_OUT) {
            booking.setIdentity_card(booking.getIdentity_card());
        } else {
            booking.setIdentity_card(transactionRequest.getImageCmnd());
        }
        // cập nhật lại trạng thái bảng booking
        Booking newBooking = bookingRepository.save(booking);

        Transaction_Info newTransactionInfo = null;
        HotelTypeRoom hotelTypeRoom =
                hotelTypeRoomRepository.findHotelTypeRoomByIdHotelAndIdTypeRoom
                        (newBooking.getId_hotel(), newBooking.getCheckinCheckouts().get(0).getTypeRoom().getId());

        // trạng thái là đã đặt cọc khi thay đổi đã nhận phòng thì tạo mới bảng hóa đơn
        if (statusRequest.equalsIgnoreCase(AppConts.DEPOSITED)) {
            Transaction_Info transaction_info = new Transaction_Info();
            transaction_info.setBooking(booking);
            transaction_info.setTotal_price(transactionRequest.getTotalPrice());
            transaction_info.setUser(userRepository.findById(transactionRequest.getId_creator()).get());
            transaction_info.setStatus(newStatus);
            transaction_info.setDescription(newStatus);
            transaction_info.setDepositPrice(booking.getDeposit_price());
            // thêm vào bảng hóa đơn
            newTransactionInfo = transactionInfoRepository.save(transaction_info);

            List<Room> rooms = roomRepository.roomsByHotelAndByTypeRoom(
                    hotelTypeRoom.getHotel().getId(), hotelTypeRoom.getTypeRoom().getId());
            for (int i = 0; i < newBooking.getTotalRooms(); i++) {
                Room room = rooms.get(i);
                room.setStatus(AppConts.USING);
                room.setDescription(AppConts.USING);
                room.setHotel(hotelTypeRoom.getHotel());
                room.setTypeRoom(hotelTypeRoom.getTypeRoom());
                roomRepository.save(room);
            }
        }
        // đã có hóa đơn rồi và thay đổi trạng thái từ nhận phòng lên trả phòng thì cập nhật lại
        else if (statusRequest.equalsIgnoreCase(AppConts.CHECK_IN)) {
            Transaction_Info transaction_info = transactionInfoRepository.findByIdBooking(newBooking.getId());
            transaction_info.setStatus(newStatus);
            transaction_info.setDescription(newStatus);
            transaction_info.setTotal_price(transactionRequest.getTotalPrice());
            newTransactionInfo = transactionInfoRepository.save(transaction_info);
        }
        // khi trạng thái là đã trả phòng thì cập nhật lại số phòng cho khách sạn
        if (newBooking.getStatus().equalsIgnoreCase(AppConts.CHECK_OUT)) {
            List<Room> rooms = roomRepository.findAllByStatusUsing(
                    hotelTypeRoom.getHotel().getId(), hotelTypeRoom.getTypeRoom().getId());
            for (int i = 0; i < newBooking.getTotalRooms(); i++) {
                Room room = rooms.get(i);
                room.setStatus(AppConts.STILLEMPTY);
                room.setDescription(AppConts.STILLEMPTY);
                room.setHotel(hotelTypeRoom.getHotel());
                room.setTypeRoom(hotelTypeRoom.getTypeRoom());
                roomRepository.save(room);
            }
            
            if (transactionRequest.isCostsIncurred()) {
                CostsIncurred costsIncurred = new CostsIncurred();
                costsIncurred.setIdTransaction(newTransactionInfo.getId());
                costsIncurred.setPrice(transactionRequest.getPriceCostsIncurred());
                costsIncurred.setDescription(transactionRequest.getNameCostsIncurred());
                costsIncurredRepository.save(costsIncurred);
            }


        }
        return getTransactionInfoResponse(newTransactionInfo);
    }


    @Override
    public List<ManagerOrderResponse> listOrderResponses(Integer idUser, String status) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<ManagerOrderResponse> orderResponseList = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAll(idUser, status);
        String offLine = "Khách đặt offline";
        for (Booking booking : bookings) {
            ManagerOrderResponse managerOrderResponse = new ManagerOrderResponse();
            Optional<VnpayTransaction> optionalVnpayTransaction = vnpayTransactionRepository.findByIdBooking(booking.getId());
            VnpayTransaction vnpayTransaction = null;
            String strCreateDate = formatter2.format(booking.getCreate_date());
            String maGd = String.valueOf(booking.getId());
            String noiDungCk = "";
            String tranDate = "";
            String amout = "";
            String soHoaDon = "";
//
            if (optionalVnpayTransaction.isPresent()) {
                vnpayTransaction = optionalVnpayTransaction.get();
//                strCreateDate = vnpayTransaction.getCreateDate();
                tranDate = vnpayTransaction.getCreateDate();
                amout = vnpayTransaction.getSoTien().toString();
                soHoaDon = vnpayTransaction.getSoHoaDon();
                // set thông tin để còn hoàn trả
                managerOrderResponse.setSoHoaDon(soHoaDon);
                managerOrderResponse.setAmout(amout);
                managerOrderResponse.setTranDate(tranDate);
                noiDungCk = vnpayTransaction.getNoidungCk();
            } else {
//                strCreateDate = formatter.format(booking.getCreate_date());
                noiDungCk = offLine;
            }

            Transaction_Info transaction_info = transactionInfoRepository.findByIdBooking(booking.getId());
            Optional<BookingHistory> checkHistory = bookingHistoryRepository.findByIdBooking(booking.getId());
            managerOrderResponse.setIdTransaction(transaction_info != null ? transaction_info.getId() : null);
            managerOrderResponse.setCreateDate(strCreateDate);
            managerOrderResponse.setIdBooking(booking.getId());
            managerOrderResponse.setStatus(booking.getStatus());
            managerOrderResponse.setFullName(booking.getFull_name());
            managerOrderResponse.setPhone(booking.getPhone());
            managerOrderResponse.setTotalPrice(booking.getTotalPrice());
            managerOrderResponse.setDepositPrice(booking.getDeposit_price());
            managerOrderResponse.setMaGd(maGd);
            managerOrderResponse.setNoiDungCk(noiDungCk);
            managerOrderResponse.setTotalRoom(booking.getTotalRooms());
            managerOrderResponse.setNameHotel(hotelRepository.findById(booking.getId_hotel()).get().getName());
            managerOrderResponse.setReason(booking.getReason());
            managerOrderResponse.setIdHotel(booking.getId_hotel());
            managerOrderResponse.setImageCmnd(booking.getIdentity_card());
            managerOrderResponse.setCheckUpdateBooking(checkHistory.isPresent() || offLine.equalsIgnoreCase(noiDungCk));
            // create date check
            managerOrderResponse.setCreateDateCheck(booking.getCreate_date());
            List<Booking_checkin_checkout> checkinCheckouts = booking.getCheckinCheckouts();
            for (Booking_checkin_checkout checkin_checkout : checkinCheckouts) {
                String strDateIn = formatter.format(checkin_checkout.getDate_in());
                String strDateOut = formatter.format(checkin_checkout.getDate_out());
                managerOrderResponse.setDateIn(strDateIn);
                managerOrderResponse.setDateOut(strDateOut);
                managerOrderResponse.setNameTypeRoom(checkin_checkout.getTypeRoom().getName());
                managerOrderResponse.setIdTypeRoom(checkin_checkout.getTypeRoom().getId());
            }
            orderResponseList.add(managerOrderResponse);
        }

        return orderResponseList;
    }

    @Override
    public StaffByHotel staffByHotel(Integer idUser) {
        StaffByHotel staffByHotel = new StaffByHotel();
        User user = userRepository.staffByHotel(idUser);
        String nameHotel = user.getHotel().getName();
        Long idHotel = user.getHotel().getId();
        String fullName = user.getFirst_name() + user.getLast_name();
        String phone = user.getPhone();
        String email = user.getEmail();

        staffByHotel.setNameHotel(nameHotel);
        staffByHotel.setEmail(email);
        staffByHotel.setPhone(phone);
        staffByHotel.setFullName(fullName);
        staffByHotel.setIdHotel(idHotel);
        return staffByHotel;
    }

    @Override
    @Transactional
    public CancelRoom cancelRoom(RefundVnpayRequest refundVnpayRequest) {
        Optional<Booking> optionalBooking = bookingRepository.findById(refundVnpayRequest.getIdBooking());
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setReason(refundVnpayRequest.getReason());
            booking.setStatus(AppConts.CANCELROOM);
            Booking newBooking = bookingRepository.save(booking);
            CancelRoom cancelRoom = new CancelRoom();
            cancelRoom.setStatus(newBooking.getStatus());
            cancelRoom.setReason(newBooking.getReason());
            cancelRoom.setIdBooking(newBooking.getId());

            if(refundVnpayRequest.isHoanHuy() == false || refundVnpayRequest.getVnp_TransDate() == null){
                // hủy phòng lưu vào bảng hóa đơn
                Transaction_Info transaction_info = new Transaction_Info();
                transaction_info.setBooking(booking);
                transaction_info.setTotal_price(booking.getDeposit_price());
                transaction_info.setUser(userRepository.findById(refundVnpayRequest.getIdUser()).get());
                transaction_info.setStatus(cancelRoom.getStatus());
                transaction_info.setDescription(cancelRoom.getStatus());
                transactionInfoRepository.save(transaction_info);
            }else {
                try {
                    String response [] = this.hoanTra(refundVnpayRequest.getAmountReq(),
                            refundVnpayRequest.getVnp_TxnRef(), refundVnpayRequest.getVnp_TransDate());
                    cancelRoom.setStatusCodeHoanHuy(response);
                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
            return cancelRoom;
        }
        return null;
    }

    @Override
    public GiaHanPhongResponse giaHanPhong(GiaHanPhongRequest giaHanPhongRequest) {
        Optional<Booking> optionalBooking = bookingRepository.findById(giaHanPhongRequest.getIdBooking());
        Long idTypeRoom = giaHanPhongRequest.getIdTypeRoom();
        Type_room typeRoom = typeRoomRepository.findById(idTypeRoom).get();
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setTotalPrice(typeRoom.getPrice() * giaHanPhongRequest.getTongNgay());
            Booking newBooking = bookingRepository.save(booking);

            Transaction_Info transaction_info = transactionInfoRepository.findByIdBooking(newBooking.getId());
            transaction_info.setTotal_price(newBooking.getTotalPrice());

            List<Booking_checkin_checkout> checkinCheckouts = newBooking.getCheckinCheckouts();
            Booking_checkin_checkout checkinCheckout = checkinCheckouts.get(0);

            checkinCheckout.setDate_in(giaHanPhongRequest.getDateIn());
            if (giaHanPhongRequest.getDateOut() != null) {
                checkinCheckout.setDate_out(giaHanPhongRequest.getDateOut());
                Booking_checkin_checkout newCheckinCheckout = bookingCheckinCheckoutRepository.save(checkinCheckout);
                GiaHanPhongResponse giaHanPhongResponse = new GiaHanPhongResponse();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String strDateIn = formatter.format(newCheckinCheckout.getDate_in());
                String strDateOut = formatter.format(newCheckinCheckout.getDate_out());

                giaHanPhongResponse.setDateIn(strDateIn);
                giaHanPhongResponse.setDateOut(strDateOut);
                giaHanPhongResponse.setTotalPrice(newBooking.getTotalPrice());
                return giaHanPhongResponse;
            }
        }
        return null;
    }

    @Override
    public Integer checkTotalRoom(Long idHotel, Long idTypeRoom, String dateIn, String dateOut) {
        HotelTypeRoom hotelTypeRoom = hotelTypeRoomRepository.checkTotalRoomHotel(idHotel, idTypeRoom);
        Integer totalRoom = 0;
        if (dateOut != null && idTypeRoom != null && dateIn != null) {
            List<Room> rooms = roomRepository.findRoomByEnabled(idHotel, idTypeRoom);
//            hotelTypeRoom = hotelTypeRoomRepository.checkTotalRoomHotel(idHotel, idTypeRoom);
            List<Booking> bookings = bookingRepository.checkTotalRoom(idHotel, idTypeRoom, dateIn, dateOut);
            List<Booking> checkTotalRoomByUsing = bookingRepository.checkTotalRoomByUsing(idHotel, idTypeRoom, dateIn, dateOut);

            for (Booking booking : bookings) {
                totalRoom += booking.getTotalRooms();
            }
            for (Booking booking : checkTotalRoomByUsing) {
                totalRoom += booking.getTotalRooms();
            }
            Integer newTotalNumberRoom = hotelTypeRoom.getTotalNumberRoom() - totalRoom - rooms.size();
            hotelTypeRoom.setTotalNumberRoom(newTotalNumberRoom < 0 ? 0 : newTotalNumberRoom);
            return hotelTypeRoom.getTotalNumberRoom();
        }
        return hotelTypeRoom == null ? 0 : hotelTypeRoom.getTotalNumberRoom();
    }

    @Override
    @Transactional
    public UpdateBookingResponse updateBooking(UpdateBookingRequest updateBookingRequest) {
        Optional<Booking> optionalBooking = bookingRepository.findById(updateBookingRequest.getIdBooking());
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            this.bookingHistorySave(booking, updateBookingRequest);
            booking.setTotalRooms(updateBookingRequest.getTotalRoom());
            Booking_checkin_checkout checkinCheckout = booking.getCheckinCheckouts().get(0);
            checkinCheckout.setDate_in(updateBookingRequest.getDateIn());
            checkinCheckout.setDate_out(updateBookingRequest.getDateOut());
            checkinCheckout.setTypeRoom(typeRoomRepository.findById(updateBookingRequest.getIdTypeRoom()).get());
//            booking.setDeposit_price(booking.getDeposit_price() + updateBookingRequest.getNewDepositPrice());
            booking.setTotalPrice(updateBookingRequest.getTotalPrice());
            Booking newBooking = bookingRepository.save(booking);
            return convertEntityToDto(newBooking);
        }
        return null;
    }

    @Override
    public Map<String, Object> getRoomAndGetUtility(Long idHotel, Long idTypeRoom) {
        Map<String, Object> roomsAndGetUtiliy = new HashMap<>();

        List<Room> roomsByUsing = roomRepository.findAllByStatusUsing(idHotel, idTypeRoom);
        List<RoomByUsingResponse> roomByUsingResponses = this.getRoomByUsingResponses(roomsByUsing);

        List<Utility> utilityList = utitlityRepository.findAll();
        List<UtilityResponse> utilityResponseList = this.getUtilityResponses(utilityList);

        roomsAndGetUtiliy.put("rooms", roomByUsingResponses);
        roomsAndGetUtiliy.put("utilityList", utilityResponseList);
        return roomsAndGetUtiliy;
    }

    @Override
    public List<OrderDetailsResponse> orderDetailsResponses(Long idTransaction) {
        return transactionInfoRepository.orderDetails(idTransaction);
    }

    @Override
    @Transactional
    public Long staffBooking(StaffBookingRequest staffBookingRequest) {
        Booking newBooking = this.saveBooking(staffBookingRequest);
        this.saveBookingCheckinCheckout(newBooking, staffBookingRequest);
        return newBooking.getId();
    }

    private void saveBookingCheckinCheckout(Booking newBooking, StaffBookingRequest staffBookingRequest) {
        Booking_checkin_checkout checkinCheckout = new Booking_checkin_checkout();
        Type_room typeRoom = typeRoomRepository.findById(staffBookingRequest.getIdTypeRoom()).get();

        checkinCheckout.setBooking(newBooking);
        checkinCheckout.setTypeRoom(typeRoom);
        checkinCheckout.setDate_in(staffBookingRequest.getDateIn());
        checkinCheckout.setDate_out(staffBookingRequest.getDateOut());
        bookingCheckinCheckoutRepository.save(checkinCheckout);
    }

    private Booking saveBooking(StaffBookingRequest staffBookingRequest) {
        Booking booking = new Booking();
        booking.setCreate_date(new Date());
        booking.setId_hotel(staffBookingRequest.getIdHotel());
        booking.setTotalPrice(staffBookingRequest.getTotalPrice());
        booking.setStatus(AppConts.DEPOSITED);
        booking.setTotalRooms(staffBookingRequest.getTotalRoom());
        booking.setEmail(staffBookingRequest.getEmail());
        booking.setFull_name(staffBookingRequest.getFullName());
        booking.setDeposit_price(staffBookingRequest.getIsOnline() ? 0 : staffBookingRequest.getDepositPrice());
        booking.setPhone(staffBookingRequest.getPhone());
        booking.setId_user(staffBookingRequest.getIdUser());
        return bookingRepository.save(booking);
    }

    private UpdateBookingResponse convertEntityToDto(Booking newBooking) {
        Booking_checkin_checkout newCheckinCheckout = newBooking.getCheckinCheckouts().get(0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDateIn = formatter.format(newCheckinCheckout.getDate_in());
        String strDateOut = formatter.format(newCheckinCheckout.getDate_out());
        UpdateBookingResponse updateBookingResponse = new UpdateBookingResponse();
        updateBookingResponse.setId(newBooking.getId());
        updateBookingResponse.setDateIn(strDateIn);
        updateBookingResponse.setDateOut(strDateOut);
        updateBookingResponse.setTotalRoom(newBooking.getTotalRooms());
        updateBookingResponse.setNameTypeRoom(newCheckinCheckout.getTypeRoom().getName());
        updateBookingResponse.setNewDepositPrice(newBooking.getDeposit_price());
        updateBookingResponse.setNewTotalPrice(newBooking.getTotalPrice());
        Optional<BookingHistory> optionalBookingHistory = bookingHistoryRepository.findByIdBooking(newBooking.getId());
        updateBookingResponse.setCheckUpdateBooking(optionalBookingHistory.isPresent());
        return updateBookingResponse;
    }

    private void bookingHistorySave(Booking booking, UpdateBookingRequest updateBookingRequest) {
        BookingHistory bookingHistory = new BookingHistory();
        Booking_checkin_checkout checkinCheckout = booking.getCheckinCheckouts().get(0);
        // hiển thị thông tin booking cũ
        bookingHistory.setTotalPrice(booking.getTotalPrice());
        bookingHistory.setTotalRoom(booking.getTotalRooms());
        bookingHistory.setDateIn(checkinCheckout.getDate_in());
        bookingHistory.setDateOut(checkinCheckout.getDate_out());
        bookingHistory.setDepositPrice(booking.getDeposit_price());
        bookingHistory.setNameTypeRoom(booking.getCheckinCheckouts().get(0).getTypeRoom().getName());
        //
        bookingHistory.setIdBooking(booking.getId());

        bookingHistory.setCreateDate(new Date());
        bookingHistory.setStatus("Đã đổi lịch");
        bookingHistory.setFullName(booking.getFull_name());
        bookingHistory.setIdHotel(booking.getId_hotel());
        bookingHistory.setIdUser(booking.getId_user().intValue());
        bookingHistory.setPhone(booking.getPhone());
        bookingHistory.setEmail(booking.getEmail());
        bookingHistory.setIdChanger(updateBookingRequest.getIdChanger());

        bookingHistoryRepository.save(bookingHistory);
    }

    private TransactionInfoResponse getTransactionInfoResponse(Transaction_Info newTransactionInfo) {
        TransactionInfoResponse transactionInfoResponse = new TransactionInfoResponse();
        transactionInfoResponse.setIdBooking(newTransactionInfo.getBooking().getId());
        transactionInfoResponse.setDate_release(newTransactionInfo.getDate_release());
        transactionInfoResponse.setId_creator(newTransactionInfo.getUser().getId());
        transactionInfoResponse.setTotal_price(newTransactionInfo.getTotal_price());
        transactionInfoResponse.setId(newTransactionInfo.getId());
        transactionInfoResponse.setStatus(newTransactionInfo.getStatus());
        transactionInfoResponse.setDescription(newTransactionInfo.getDescription());

        return transactionInfoResponse;
    }

    private List<RoomByUsingResponse> getRoomByUsingResponses(List<Room> roomsByUsing) {
        return roomsByUsing.stream().map(room ->
                modelMapper.map(room, RoomByUsingResponse.class)).collect(Collectors.toList());
    }

    private List<UtilityResponse> getUtilityResponses(List<Utility> utilityList) {
        return utilityList.stream().map(utility -> modelMapper.map(utility, UtilityResponse.class
        )).collect(Collectors.toList());
    }

    private String[] hoanTra (String amountReq , String vnp_TxnRef, String vnp_TransDate ) throws Exception {
        String vnp_TmnCode = ConfigVnPay.vnp_TmnCode;
        String vnp_IpAddr = "192.168.1.1";
        int amount = Integer.parseInt(amountReq) * 100;
        String email = "Cuongpqph11972@fpt.edu.vn";
        String tranType = "02";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "refund");
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Kiem tra ket qua GD OrderId:" + vnp_TxnRef);
        vnp_Params.put("vnp_TransDate", vnp_TransDate);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateBy", email);
        vnp_Params.put("vnp_TransactionType", tranType);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = ConfigVnPay.hmacSHA512(ConfigVnPay.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = ConfigVnPay.vnp_apiUrl + "?" + queryUrl;
        URL url = new URL(paymentUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String Rsp = response.toString();
        String respDecode = URLDecoder.decode(Rsp, StandardCharsets.UTF_8);
        String[] responseData = respDecode.split("&|\\=");
        return responseData;
    }
}
