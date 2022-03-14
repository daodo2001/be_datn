package com.fpt.hotel.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "hotel_type_rooms")
public class HotelTypeRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer totalNumberRoom;

    @ManyToOne
    @JoinColumn(name = "id_hotel")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "id_type_room")
    private Type_room typeRoom;
}
