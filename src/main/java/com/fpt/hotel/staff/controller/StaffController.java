package com.fpt.hotel.staff.controller;

import com.fpt.hotel.config.AppConts;
import com.fpt.hotel.config.ConfigVnPay;
import com.fpt.hotel.model.VnpayTransaction;
import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.repository.VnpayTransactionRepository;
import com.fpt.hotel.staff.dto.request.GiaHanPhongRequest;
import com.fpt.hotel.staff.dto.request.RefundVnpayRequest;
import com.fpt.hotel.staff.dto.request.TransactionRequest;
import com.fpt.hotel.staff.dto.request.UpdateBookingRequest;
import com.fpt.hotel.staff.dto.response.*;
import com.fpt.hotel.staff.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@PreAuthorize("hasRole('STAFF')")
@RequestMapping("api/staff")
@CrossOrigin("*")
public class StaffController {

    @Autowired
    StaffService staffService;
    UpdateBookingRequest updateBookingRequestUpdate;
    @Autowired
    VnpayTransactionRepository vnpayTransactionRepository;

    @PostMapping("confirm-booking")
    public ResponseEntity<ResponseObject> confirmBooking(
            @RequestBody TransactionRequest transactionRequest) {
        TransactionInfoResponse transactionInfoResponse = staffService.confirmBooking(transactionRequest);
        if (transactionInfoResponse.getStatus().equalsIgnoreCase(AppConts.CHECK_IN)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.name(),
                            "Xác nhận đã nhận phòng thành công", transactionInfoResponse));
        } else if (transactionInfoResponse.getStatus().equalsIgnoreCase(AppConts.CHECK_OUT)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.name(),
                            "Xác nhận đã trả phòng thành công", transactionInfoResponse));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Tạo hóa đơn thất bại", null));
    }

    @GetMapping("booking-order")
    public ResponseEntity<ResponseObject> bookingOrder(@RequestParam("idUser") Integer id,
                                                       @RequestParam("status") String status) {
        List<ManagerOrderResponse> listOrderResponses = staffService.listOrderResponses(id, status);
        if (!listOrderResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.name(), "Trả về dữ liệu người dùng đặt phòng thành công", listOrderResponses));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu", null));
    }

    @GetMapping("staff-by-hotel")
    public ResponseEntity<ResponseObject> staffByHotel(@RequestParam("idUser") Integer id) {
        StaffByHotel staffByHotel = staffService.staffByHotel(id);
        if (staffByHotel.getNameHotel() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Không có dữ liệu", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.name(), "Trả về tên cơ sở của nhân viên thành công", staffByHotel));
    }

    @PutMapping("cancel-room/{id}")
    public ResponseEntity<ResponseObject> cancelRoom(@RequestBody RefundVnpayRequest refundVnpayRequest) {
        CancelRoom cancelRoom = staffService.cancelRoom(refundVnpayRequest);
        if (cancelRoom == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Hủy phòng thất bại", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.name(), "Hủy phòng thành công", cancelRoom));
    }

    @PutMapping("gia-han-phong")
    public ResponseEntity<ResponseObject> giaHanPhong(@RequestBody GiaHanPhongRequest giaHanPhongRequest) {
        GiaHanPhongResponse giaHanPhongResponse = staffService.giaHanPhong(giaHanPhongRequest);
        if (giaHanPhongResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Gia hạn phòng thất bại", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.name(), "Gia hạn phòng thành công", giaHanPhongResponse));
    }

    @GetMapping("check-total-room")
    public ResponseEntity<ResponseObject> checkTotalRoom(@RequestParam("idHotel") Long idHotel,
                                                         @RequestParam(value = "idTypeRoom", required = false) Long idTypeRoom,
                                                         @RequestParam(value = "dateIn", required = false) String dateIn,
                                                         @RequestParam(value = "dateOut", required = false) String dateOut) {
        Integer checkTotalRoom = staffService.checkTotalRoom(idHotel, idTypeRoom, dateOut, dateIn);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Trả về tổng số phòng còn trống thành công", checkTotalRoom));
    }

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(@RequestBody UpdateBookingRequest updateBookingRequest) throws IOException {

//        if (updateBookingRequest.getNewDepositPrice() != 0) {
//            this.updateBookingRequestUpdate = updateBookingRequest;
//
//            String vnp_Version = "2.0.1";
//            String vnp_Command = "pay";
//            String vnp_OrderInfo = updateBookingRequest.getGhiChu();
//            String orderType = "170000";
//            String vnp_TxnRef = ConfigVnPay.getRandomNumber(8);
//            String vnp_IpAddr = "192.168.1.1";
//            String vnp_TmnCode = ConfigVnPay.vnp_TmnCode;
//
//            int amount = updateBookingRequest.getNewDepositPrice() * 100;
//            Map<String, String> vnp_Params = new HashMap<>();
//            vnp_Params.put("vnp_Version", vnp_Version);
//            vnp_Params.put("vnp_Command", vnp_Command);
//            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//            vnp_Params.put("vnp_Amount", String.valueOf(amount));
//            vnp_Params.put("vnp_CurrCode", "VND");
//
//            String bank_code = "NCB";
//            if (bank_code != null && !bank_code.isEmpty()) {
//                vnp_Params.put("vnp_BankCode", bank_code);
//            }
//            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
//            vnp_Params.put("vnp_OrderType", orderType);
//            vnp_Params.put("vnp_ReturnUrl", "http://localhost:3000/admin/staff/thong-tin-thanh-toan");
//            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//            vnp_Params.put("vnp_Locale", "vn");
//            Date date = new Date();
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//            String vnp_CreateDate = formatter.format(date);
//
//            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//            List fieldNames = new ArrayList(vnp_Params.keySet());
//            Collections.sort(fieldNames);
//            StringBuilder hashData = new StringBuilder();
//            StringBuilder query = new StringBuilder();
//            Iterator itr = fieldNames.iterator();
//            while (itr.hasNext()) {
//                String fieldName = (String) itr.next();
//                String fieldValue = vnp_Params.get(fieldName);
//                if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                    //Build hash data
//                    hashData.append(fieldName);
//                    hashData.append('=');
//                    hashData.append(fieldValue);
//                    //Build query
//                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                    query.append('=');
//                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                    if (itr.hasNext()) {
//                        query.append('&');
//                        hashData.append('&');
//                    }
//                }
//            }
//            String queryUrl = query.toString();
//            String vnp_SecureHash = ConfigVnPay.hmacSHA512(ConfigVnPay.vnp_HashSecret, hashData.toString());
//            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//            String paymentUrl = ConfigVnPay.vnp_PayUrl + "?" + queryUrl;
//
//            ResponseObject responseObject = new ResponseObject("00", "success", paymentUrl);
//
//            return ResponseEntity.ok(responseObject);
//        } else {
//            UpdateBookingResponse updateBookingResponse = staffService.updateBooking(updateBookingRequest);
//            ResponseObject responseObject = new ResponseObject(HttpStatus.OK.name(), "Đổi lịch thành công", updateBookingResponse);
//            return ResponseEntity.ok(responseObject);
//        }
        UpdateBookingResponse updateBookingResponse = staffService.updateBooking(updateBookingRequest);
        ResponseObject responseObject = new ResponseObject(HttpStatus.OK.name(), "Đổi lịch thành công", updateBookingResponse);
        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("thong-tin-thanh-toan")
    public ResponseEntity<?> thongTinThanhToan(
            @RequestParam(value = "vnp_Amount", required = false) String amout,
            @RequestParam(value = "vnp_BankCode", required = false) String bankCode,
            @RequestParam(value = "vnp_BankTranNo", required = false) String bankTranNo,
            @RequestParam(value = "vnp_CardType", required = false) String cartType,
            @RequestParam(value = "vnp_OrderInfo", required = false) String orDerInfo,
            @RequestParam(value = "vnp_PayDate", required = false) String payDate,
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            @RequestParam(value = "vnp_TmnCode", required = false) String tmnCode,
            @RequestParam(value = "vnp_TransactionNo", required = false) String transactionNo,
            @RequestParam(value = "vnp_TransactionStatus", required = false) String transactionStatus,
            @RequestParam(value = "vnp_TxnRef", required = false) String txnRef,
            @RequestParam(value = "vnp_SecureHash", required = false) String secureHash
    ) throws Exception {
        ResponseObject responseObject = null;
        if (responseCode.equalsIgnoreCase("24")) {
            responseObject = new ResponseObject();
            responseObject.setData(null);
            responseObject.setMessage("Giao dịch không thành công");
            responseObject.setStatus(HttpStatus.BAD_REQUEST.name());
            return ResponseEntity.badRequest().body(responseObject);
        }
        Optional<VnpayTransaction> checkHoaDonVnpay = vnpayTransactionRepository.findBySoHoaDon(bankTranNo);
        Optional<VnpayTransaction> vnpayTransactionOptional =
                vnpayTransactionRepository.findByIdBooking(this.updateBookingRequestUpdate.getIdBooking());
        VnpayTransaction newVnpayTransaction = null;

        if (checkHoaDonVnpay.isEmpty()) {
            VnpayTransaction vnpayTransaction = vnpayTransactionOptional.get();
            vnpayTransaction.setBankCode(bankCode);
            vnpayTransaction.setNoidungCk(orDerInfo);
            // lưu số hóa đơn để hoàn trả
            vnpayTransaction.setMaGd(txnRef);
            vnpayTransaction.setSoHoaDon(bankTranNo);
            vnpayTransaction.setStatus(transactionStatus);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = sdf1.parse(payDate);
            // Now format the above date as needed...
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String ngayTao = sdf2.format(date);
            vnpayTransaction.setCreateDate(payDate);
            UpdateBookingResponse updateBookingResponse = staffService.updateBooking(updateBookingRequestUpdate);
            vnpayTransaction.setIdBooking(updateBookingResponse.getId());
            vnpayTransaction.setSoTien(Double.parseDouble(amout) / 100);
            newVnpayTransaction = vnpayTransactionRepository.save(vnpayTransaction);
        } else {
            newVnpayTransaction = checkHoaDonVnpay.get();
        }

        responseObject = new ResponseObject(HttpStatus.OK.name(), "Đổi lịch thành công", newVnpayTransaction);

        return ResponseEntity.ok().body(responseObject);
    }

    @GetMapping("transaction-utility")
    public ResponseEntity<ResponseObject> getRoomsAndUtility(
            @RequestParam("idHotel") Long idHotel,
            @RequestParam("idTypeRoom") Long idTypeRoom) {
        Map<String, Object> roomsAndUtilityResponse = staffService.getRoomAndGetUtility(idHotel, idTypeRoom);
        if (roomsAndUtilityResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có dữ liệu!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Trả về dữ liệu thành công!", roomsAndUtilityResponse)
        );
    }

    @GetMapping("order-details")
    public ResponseEntity<ResponseObject> orderDetails(@RequestParam("idTransaction") Long idTransaction) {
        List<OrderDetailsResponse> orderDetailsResponses = staffService.orderDetailsResponses(idTransaction);
        if (orderDetailsResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có dữ liệu!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Trả về dữ liệu thành công!", orderDetailsResponses)
        );
    }

    @GetMapping("hoan-tra")
    public ResponseEntity<?> hoanTra(
            @RequestParam(value = "order_id", required = false) String vnp_TxnRef,
            @RequestParam(value = "trans_date", required = false) String vnp_TransDate,
            @RequestParam(value = "amount", required = false) String amountReq
    ) throws Exception {
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
        return ResponseEntity.ok().body(responseData);
    }
}
