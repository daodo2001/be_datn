package com.fpt.hotel.repository;

import com.fpt.hotel.model.Type_room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRoomRepository extends JpaRepository<Type_room , Long> {
}
