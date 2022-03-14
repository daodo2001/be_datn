package com.fpt.hotel.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date create_date;

    private double deposit_price;

    private String full_name;

    private String email;

    private String phone;

    private String status;

    private double discount;

    private Long id_user;

    @OneToMany(mappedBy = "booking")
    private List<Booking_checkin_checkout> checkinCheckouts;

    @OneToMany(mappedBy = "booking")
    private List<Transaction_Info> id_transaction_info;

    private Long id_hotel;

    private Double totalPrice;

    private Integer totalPeoples;

    private Integer totalRooms;

    private String reason;

    @Lob
    @Column( length = 100000 )
    private String identity_card;
}
