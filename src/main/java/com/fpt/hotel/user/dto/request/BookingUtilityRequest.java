package com.fpt.hotel.user.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingUtilityRequest {

    private String description;

    private Long idBooking;

    private List<UtilityRequest> utilityRequests;

}
