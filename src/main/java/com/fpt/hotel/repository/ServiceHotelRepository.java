package com.fpt.hotel.repository;

import com.fpt.hotel.model.ServiceHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceHotelRepository extends JpaRepository<ServiceHotel, Long> {

    Boolean existsByName(String name);
}
