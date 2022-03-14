package com.fpt.hotel.staff.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderServiceRequest {

    private Long idRoom;

    private Long idBooking;

    private List<ServiceRequest> serviceRequestList;
}
