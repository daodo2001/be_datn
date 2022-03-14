package com.fpt.hotel.staff.dto.response;

public interface BookingByCheckIn {

    String getFullName();

    String getPhone();

    String getDateIn();

    String getDateOut();

    String getStatus();

    String getNameTypeRoom();

    Integer getTotalRoom();

    Long getIdTypeRoom();

    Long getIdHotel();

    Long getIdBooking();
}
