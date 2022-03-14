package com.fpt.hotel.staff.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffByHotel {

    private Long idHotel;

    private String nameHotel;

    private String fullName;

    private String phone;

    private String email;

}
