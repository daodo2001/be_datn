package com.fpt.hotel.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 50, message = "Base name exceeds 50 characters")
    private String name;
    private String city;
    private String address;
    private String images;

    private Integer isEnabled;

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    private List<User> users;

    @OneToMany(mappedBy = "hotel")
    private List<HotelTypeRoom> hotelTypeRooms;

    @OneToMany(mappedBy = "hotels",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;

}
