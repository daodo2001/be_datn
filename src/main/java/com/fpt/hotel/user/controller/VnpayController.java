package com.fpt.hotel.user.controller;

import com.fpt.hotel.config.ConfigVnPay;
import com.fpt.hotel.model.VnpayTransaction;
import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.repository.VnpayTransactionRepository;
import com.fpt.hotel.user.dto.request.BookingRequest;
import com.fpt.hotel.user.dto.response.BookingResponse;
import com.fpt.hotel.user.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("api")
public class VnpayController {

    @Autowired
    BookingService bookingService;

    @Autowired
    VnpayTransactionRepository vnpayTransactionRepository;

    BookingRequest bookingRequestSave;

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(
            @RequestParam("amout") int amout,
            @RequestParam("description") String description,
            @RequestParam("bankCode") String bankCode,
            @RequestBody BookingRequest bookingRequest) throws IOException {

        this.bookingRequestSave = bookingRequest;

        String vnp_Version = "2.0.1";
        String vnp_Command = "pay";
        String vnp_OrderInfo = description;
        String orderType = "170000";
        String vnp_TxnRef = ConfigVnPay.getRandomNumber(8);
        String vnp_IpAddr = "192.168.1.1";
        String vnp_TmnCode = ConfigVnPay.vnp_TmnCode;

        int amount = amout * 100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        String bank_code = bankCode;
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", ConfigVnPay.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", "vn");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(date);

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

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
                hashData.append(fieldValue);
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
        String paymentUrl = ConfigVnPay.vnp_PayUrl + "?" + queryUrl;

        ResponseObject responseObject = new ResponseObject("00", "success", paymentUrl);

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
        Optional<VnpayTransaction> optionalVnpayTransaction = vnpayTransactionRepository.findBySoHoaDon(bankTranNo);
        VnpayTransaction newVnpayTransaction = null;
        if (optionalVnpayTransaction.isEmpty()) {
            VnpayTransaction vnpayTransaction = new VnpayTransaction();
            vnpayTransaction.setBankCode(bankCode);
            vnpayTransaction.setNoidungCk(orDerInfo);
            vnpayTransaction.setMaGd(transactionNo);
            vnpayTransaction.setSoHoaDon(txnRef);
            vnpayTransaction.setStatus(transactionStatus);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = sdf1.parse(payDate);
            // Now format the above date as needed...
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String ngayTao = sdf2.format(date);
            vnpayTransaction.setCreateDate(payDate);
            BookingResponse bookingResponse = bookingService.create(bookingRequestSave);
            vnpayTransaction.setIdBooking(bookingResponse.getId());
            vnpayTransaction.setSoTien(bookingResponse.getDeposit_price());
            newVnpayTransaction = vnpayTransactionRepository.save(vnpayTransaction);
        } else {
            newVnpayTransaction = optionalVnpayTransaction.get();
        }

        responseObject = new ResponseObject(HttpStatus.OK.name(), "Dat phong thanh cong", newVnpayTransaction);

        return ResponseEntity.ok().body(responseObject);
    }
}
