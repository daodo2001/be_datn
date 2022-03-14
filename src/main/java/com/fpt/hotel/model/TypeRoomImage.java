package com.fpt.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "typeRoomImages")
@Getter
@Setter
public class TypeRoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String image;

    @ManyToOne
    @JoinColumn(name = "id_type_room")
    @JsonIgnore
    private Type_room typeRoom;
}
