package com.fpt.hotel.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "booking_history")
public class BookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idHotel;

    private Long idBooking;

    private Integer idUser;

    private String fullName;

    private String email;

    @Temporal(TemporalType.DATE)
    private Date dateIn;

    @Temporal(TemporalType.DATE)
    private Date dateOut;

    private Date createDate;

    private String phone;

    private Double totalPrice;

    private Double depositPrice;

    private String status;

    private Integer idChanger;

    private Integer totalRoom;

    private String nameTypeRoom;


}
