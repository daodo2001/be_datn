package com.fpt.hotel.repository;

import com.fpt.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Boolean existsByNumberRoom(String numberRoom);

    @Query(value = "SELECT * FROM ROOMS where id_hotel = ?1 and id_type_room = ?2 and status = 'Còn Trống' and DESCRIPTION = 'Còn Trống'" , nativeQuery = true)
    List<Room> roomsByHotelAndByTypeRoom(Long idHotel , Long idTypeRoom);

    @Query(value = "SELECT * FROM ROOMS where id_hotel = ?1 and id_type_room = ?2 and status = 'Đang sử dụng'" , nativeQuery = true)
    List<Room> findAllByStatusUsing(Long idHotel , Long idTypeRoom);

    @Query(value = "SELECT * FROM ROOMS where id_hotel = ?1 and id_type_room = ?2",nativeQuery = true)
    List<Room> findAll(Long idHotel , Long idTypeRoom);

    @Query(value = "SELECT * FROM ROOMS where id_hotel = ?1 ",nativeQuery = true)
    List<Room> findAll(Long idHotel);

    @Query(value = "SELECT * FROM ROOMS where id_hotel = ?1 and id_type_room = ?2 and enabled = false",nativeQuery = true)
    List<Room> findRoomByEnabled(Long idHotel , Long idTypeRoom);
}
