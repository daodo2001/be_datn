package com.fpt.hotel.repository;

import com.fpt.hotel.model.TypeRoomImage;
import com.fpt.hotel.model.Type_room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRoomImageRepository extends JpaRepository<TypeRoomImage, Integer> {
}
