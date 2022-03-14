package com.fpt.hotel.repository;

import com.fpt.hotel.model.TransactionServiceHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionServiceHotelRepository extends JpaRepository<TransactionServiceHotel, Long> {

    @Query(value = "SELECT * FROM " +
            "`transaction_service` " +
            "WHERE id_room = ?1 and id_service_hotel = ?2 and id_transaction = ?3", nativeQuery = true)
    Optional<TransactionServiceHotel> findTransactionServiceHotel(Long idRoom , Long idServiceHotel , Long idTransaction);
}
