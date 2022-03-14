package com.fpt.hotel.repository;

import com.fpt.hotel.model.Hotel;
import com.fpt.hotel.owner.dto.response.Total;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Boolean existsByName(String name);

    @Query(value = "SELECT count(id) as total FROM HOTELS ",nativeQuery = true)
    Total totalHotel();

}
