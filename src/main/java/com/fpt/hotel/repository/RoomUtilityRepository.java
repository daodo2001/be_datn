package com.fpt.hotel.repository;

import com.fpt.hotel.model.RoomUtility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomUtilityRepository extends JpaRepository<RoomUtility , Integer> {

    @Query(value = "SELECT * FROM `room_utilities` WHERE id_room = ?1",nativeQuery = true)
    List<RoomUtility> findAllByIdRoom(Long idRoom);

    @Query(value = "SELECT * FROM `room_utilities` WHERE id_room = ?1 and id_utility = ?2" , nativeQuery = true)
    Optional<RoomUtility> findByIdRoomAndIdUtility(Long idRoom , Long idUtility);
}
