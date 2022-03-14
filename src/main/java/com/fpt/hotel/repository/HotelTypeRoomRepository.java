package com.fpt.hotel.repository;

import com.fpt.hotel.model.Hotel;
import com.fpt.hotel.model.HotelTypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelTypeRoomRepository extends JpaRepository<HotelTypeRoom , Integer> {

    @Query(value =  "SELECT * FROM HOTEL_TYPE_ROOMS where id_hotel = ?1 and id_type_room = ?2", nativeQuery = true)
    List<HotelTypeRoom> checkHotelTypeRoom(Long idHotel, Long idTypeRoom);

    @Query(value =  "SELECT * FROM HOTEL_TYPE_ROOMS where id_hotel = ?1", nativeQuery = true)
    List<HotelTypeRoom> hotelTypeRoomFindByIdHotel(Long idHotel);

    @Query(value = "SELECT * FROM HOTEL_TYPE_ROOMS \n" +
            "join BOOKING_CHECKIN_CHECKOUTS on BOOKING_CHECKIN_CHECKOUTS.ID_TYPE_ROOM = HOTEL_TYPE_ROOMS.ID_TYPE_ROOM\n" +
            "join BOOKINGS on BOOKINGS.ID = BOOKING_CHECKIN_CHECKOUTS.ID_BOOKING\n" +
            "where HOTEL_TYPE_ROOMS.ID_HOTEL = ?1 and HOTEL_TYPE_ROOMS.ID_TYPE_ROOM = ?2" , nativeQuery = true)
    HotelTypeRoom findHotelTypeRoomByIdHotelAndIdTypeRoom(Long idHotel , Long idTypeRoom);

    @Query(value = "SELECT * FROM HOTEL_TYPE_ROOMS where id_hotel = ?1 and id_type_room = ?2",nativeQuery = true)
    HotelTypeRoom checkTotalRoomHotel(Long idHotel , Long idTypeRoom);

    @Query(value = "SELECT * FROM `hotel_type_rooms` WHERE id_hotel = ?1",nativeQuery = true)
    List<HotelTypeRoom> findAll(Long idHotel);
}
