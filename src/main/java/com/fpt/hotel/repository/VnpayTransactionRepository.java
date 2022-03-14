package com.fpt.hotel.repository;

import com.fpt.hotel.model.VnpayTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VnpayTransactionRepository extends JpaRepository<VnpayTransaction , Long> {

    @Query(value = "SELECT * FROM VN_PAY_TRANSACTIONS where id_booking = ?1", nativeQuery = true)
    Optional<VnpayTransaction> findByIdbooking(Long idBooking);

//    @Query(value = "SELECT * FROM VN_PAY_TRANSACTIONS \n" +
//            "join BOOKINGS  on BOOKINGS.id = VN_PAY_TRANSACTIONS.ID_BOOKING\n" +
//            "join BOOKING_CHECKIN_CHECKOUTS on BOOKING_CHECKIN_CHECKOUTS.ID_BOOKING =  BOOKINGS.id \n" +
//            "where BOOKINGS.STATUS  = ?1", nativeQuery = true)
    @Query(value = "SELECT * FROM VN_PAY_TRANSACTIONS\n" +
            "            join BOOKINGS  on BOOKINGS.id = VN_PAY_TRANSACTIONS.ID_BOOKING\n" +
            "            join BOOKING_CHECKIN_CHECKOUTS on BOOKING_CHECKIN_CHECKOUTS.ID_BOOKING =  BOOKINGS.id \n" +
            "            join hotels on hotels.id = bookings.id_hotel\n" +
            "            join users on users.id_hotel = hotels.id\n" +
            "            where users.id = ?1 and bookings.status = ?2", nativeQuery = true)
    List<VnpayTransaction> listVnpayTransaction(Integer idUser ,String status);

    @Query(value = "SELECT * FROM VN_PAY_TRANSACTIONS where VN_PAY_TRANSACTIONS.SO_HOA_DON = ?1",nativeQuery = true)
    Optional<VnpayTransaction> findBySoHoaDon(String soHoaDon);

    @Query(value = "SELECT * FROM VN_PAY_TRANSACTIONS where id_booking = ?1",nativeQuery = true)
    Optional<VnpayTransaction> findByIdBooking(Long idBooking);
}
