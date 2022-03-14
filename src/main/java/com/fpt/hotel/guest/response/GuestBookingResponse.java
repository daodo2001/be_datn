package com.fpt.hotel.guest.response;

public interface GuestBookingResponse {

    String getNameTypeRoom();

    String getNameHotel();

    String getDateIn();

    String getDateOut();

    Integer getTotalRooms();

    Integer getTotalPrice();

    Long getId();
}
