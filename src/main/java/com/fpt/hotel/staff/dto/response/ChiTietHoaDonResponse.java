package com.fpt.hotel.staff.dto.response;

public interface ChiTietHoaDonResponse {

    String getFullName();

    String getDateIn();

    String getDateOut();

    Integer getTotalRoom();

    Double getTotalPrice();

    String getNameHotel();

    String getPhone();

    String getNameTypeRoom();

    Long getIdTransaction();

    String getStatus();

    Double getDepositPrice();

    String getNameCosts();

    Double getPriceCosts();
}
