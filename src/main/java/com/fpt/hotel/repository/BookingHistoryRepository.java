package com.fpt.hotel.repository;

import com.fpt.hotel.model.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory , Long> {

    @Query(value = "SELECT * FROM BOOKING_HISTORY where id_changer = ?1",nativeQuery = true)
    List<BookingHistory> findAll(Integer idChanger);

    @Query(value = "SELECT * FROM BOOKING_HISTORY where id_booking = ?1",nativeQuery = true)
    Optional<BookingHistory> findByIdBooking(Long idBooking);

    @Query(value = "SELECT * FROM BOOKING_HISTORY where id_hotel = ?1" , nativeQuery = true)
    List<BookingHistory> findAll(Long idHotel);
}
