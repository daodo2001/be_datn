package com.fpt.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "booking_checkin_checkouts")
public class Booking_checkin_checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date_in;
    @Temporal(TemporalType.DATE)
    private Date date_out;

    @ManyToOne
    @JoinColumn(name = "id_booking")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "id_type_room")
    private Type_room typeRoom;

}
