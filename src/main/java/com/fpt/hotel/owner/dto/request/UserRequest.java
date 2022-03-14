package com.fpt.hotel.owner.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
public class UserRequest {
    private Integer id;
    private String first_name;
    private String last_name;
    private String phone;
    private String email;

    @Temporal(TemporalType.DATE)
    private Date date_of_birth;
    private String address;
    private Integer enabled;
    private String image;
    private Integer sex;
    private Integer status;
    private String username;
    private String password;
    private String cccd;
    private Integer id_creator;
    private Long id_hotel;
}
