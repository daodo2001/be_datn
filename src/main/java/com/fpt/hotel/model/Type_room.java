package com.fpt.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "type_rooms")
@Getter
@Setter
public class Type_room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;

    private int capacity;

    private String description;

    private double size;

    private String status;

    @OneToMany(mappedBy = "typeRoom")
    private List<Room> rooms;

    @OneToMany(mappedBy = "typeRoom")
    private List<Booking_checkin_checkout> booking_checkin_checkouts ;

    @OneToMany(mappedBy = "typeRoom")
    private List<TypeRoomImage> typeRoomImages;

    @OneToMany(mappedBy = "typeRoom")
    private List<HotelTypeRoom> hotelTypeRooms;
}
