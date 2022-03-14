package com.fpt.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "transaction_infos")
public class Transaction_Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date date_release;

    private double total_price;

    private Double depositPrice;

    @ManyToOne
    @JoinColumn(name = "id_booking")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "id_creator")
    private User user;

    private String status;

    private String description;
}
