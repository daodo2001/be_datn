package com.fpt.hotel.repository;

import com.fpt.hotel.guest.response.GuestBookingResponse;
import com.fpt.hotel.model.Booking;
import com.fpt.hotel.owner.dto.response.*;
import com.fpt.hotel.staff.dto.response.BookingByCheckIn;
import com.fpt.hotel.staff.dto.response.ChiTietHoaDonResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT * FROM BOOKINGS where id_user = ?1", nativeQuery = true)
    List<Booking> BookingOrderResponseList(Integer id);

    @Query(value = "SELECT * FROM BOOKING_CHECKIN_CHECKOUTS\n" +
            "            join bookings on bookings.id = BOOKING_CHECKIN_CHECKOUTS.ID_BOOKING \n" +
            "            WHERE id_hotel = ?1 and id_type_room = ?2" +
            "            and status = 'Đã đặt cọc' and DATE_IN <= ?3  and DATE_OUT  >= ?4", nativeQuery = true)
    List<Booking> checkTotalRoom(Long idHotel, Long idTypeRoom,
                                 String dateOut, String dateIn);


    @Query(value = "SELECT * FROM BOOKING_CHECKIN_CHECKOUTS\n" +
            "            join bookings on bookings.id = BOOKING_CHECKIN_CHECKOUTS.ID_BOOKING \n" +
            "            WHERE id_hotel = ?1 and id_type_room = ?2" +
            "            and status =  'Đã nhận phòng' and DATE_IN <= ?3  and DATE_OUT  >= ?4", nativeQuery = true)
    List<Booking> checkTotalRoomByUsing(Long idHotel, Long idTypeRoom,
                                        String dateOut, String dateIn);

    @Query(value = "SELECT username , count(bookings.id) as totalOrder , sum(TOTAL_PRICE) as totalPrice FROM BOOKINGS \n" +
            "join users on users.id = bookings.id_user\n" +
            "group by  username\n" +
            "order by totalOrder  desc", nativeQuery = true)
    List<TopUserOrder> topUserOrder();

    @Query(value = "SELECT count(id_hotel) as totalOrder , hotels.name , sum(total_price) as totalPrice  FROM BOOKINGS \n" +
            "join hotels on hotels.id = bookings.id_hotel\n" +
            "group by hotels.name\n" +
            "order by totalOrder desc", nativeQuery = true)
    List<TopHotelOrder> topHotelOrder();

    @Query(value = "SELECT * FROM BOOKINGS \n" +
            "join BOOKING_CHECKIN_CHECKOUTS on BOOKING_CHECKIN_CHECKOUTS.ID_BOOKING =  BOOKINGS.id\n" +
            "join hotels on hotels.id = bookings.id_hotel\n" +
            "join users on users.id_hotel = hotels.id\n" +
            "where users.id = ?1 and bookings.status = ?2", nativeQuery = true)
    List<Booking> findAll(Integer idUser, String status);

    @Query(value = "SELECT full_name as fullName , bookings.phone , date_in as dateIn , date_out as dateOut , bookings.status , \n" +
            "type_rooms.name as nameTypeRoom , bookings.total_rooms as totalRoom , hotels.id as idHotel , " +
            "type_rooms.id as idTypeRoom , bookings.id as idBooking  FROM `bookings`\n" +
            "           join booking_checkin_checkouts on booking_checkin_checkouts.id_booking = bookings.id\n" +
            "           join type_rooms on type_rooms.id = booking_checkin_checkouts.id_type_room\n" +
            "           join hotels on hotels.id = bookings.id_hotel\n" +
            "           join users on users.id_hotel = hotels.id\n" +
            "           where users.id = ?1 and bookings.status = \"Đã nhận phòng\"", nativeQuery = true)
    List<BookingByCheckIn> listBookingByCheckIn(Integer idUser);

    @Query(value = "SELECT\n" +
            "    full_name AS fullName,\n" +
            "    date_in AS dateIn,\n" +
            "    date_out AS dateOut,\n" +
            "    total_rooms AS totalRoom,\n" +
            "    bookings.total_price AS totalPrice,\n" +
            "    hotels.name AS nameHotel,\n" +
            "    bookings.phone,\n" +
            "    type_rooms.name AS nameTypeRoom,\n" +
            "    transaction_infos.id AS idTransaction,\n" +
            "    bookings.status,\n" +
            "    transaction_infos.deposit_price AS depositPrice ,\n" +
            "    costs_incurred.description as nameCosts ,\n" +
            "    costs_incurred.price as priceCosts\n" +
            "FROM\n" +
            "    `bookings`\n" +
            "JOIN booking_checkin_checkouts ON booking_checkin_checkouts.id_booking = bookings.id\n" +
            "JOIN type_rooms ON type_rooms.id = booking_checkin_checkouts.id_type_room\n" +
            "JOIN hotels ON hotels.id = bookings.id_hotel\n" +
            "JOIN users ON users.id_hotel = hotels.id\n" +
            "JOIN transaction_infos ON transaction_infos.id_booking = bookings.id\n" +
            "LEFT JOIN costs_incurred on costs_incurred.id_transaction = transaction_infos.id\n" +
            "WHERE\n" +
            "    bookings.status = \"Đã trả phòng\" AND bookings.id = ?1 AND users.id = ?2", nativeQuery = true)
    ChiTietHoaDonResponse chiTietHoaDon(Long idBooking, Integer idUser);

    @Query(value = "SELECT count(bookings.id) as total  FROM bookings" , nativeQuery = true)
    Total totalBookings();

    @Query(value = "SELECT full_name as fullName , phone , bookings.status , DATE_FORMAT(date_in, '%d/%m/%Y') as dateIn ,  " +
            "DATE_FORMAT(date_out, '%d/%m/%Y') as dateOut , total_rooms as totalRooms , type_rooms.name as nameTypeRoom , hotels.name as nameHotel FROM `bookings`\n" +
            "join booking_checkin_checkouts on booking_checkin_checkouts.id_booking = bookings.id\n" +
            "join hotels on hotels.id = bookings.id_hotel\n" +
            "join type_rooms on type_rooms.id = booking_checkin_checkouts.id_type_room\n" +
            " WHERE  bookings.status = ?1 and id_hotel = ?2",nativeQuery = true)
    List<ManageOrderBookingsResponse> bookingsByStatusAndIdHotel(String status , Long idHotel);

    @Query(value = "SELECT full_name as fullName , phone , bookings.status , DATE_FORMAT(date_in, '%d/%m/%Y') as dateIn ,  DATE_FORMAT(date_out, '%d/%m/%Y') as dateOut , total_rooms as totalRooms , type_rooms.name as nameTypeRoom , hotels.name as nameHotel FROM `bookings`\n" +
            "join booking_checkin_checkouts on booking_checkin_checkouts.id_booking = bookings.id\n" +
            "join hotels on hotels.id = bookings.id_hotel\n" +
            "join type_rooms on type_rooms.id = booking_checkin_checkouts.id_type_room\n" +
            " WHERE  bookings.status = ?1 ",nativeQuery = true)
    List<ManageOrderBookingsResponse> bookingsByStatusAndIdHotel(String status);

    @Query(value = "SELECT\n" +
            "    hotels.name AS nameHotel,\n" +
            "    type_rooms.name AS nameTypeRoom,\n" +
            "    DATE_FORMAT(date_in, '%d/%m/%Y') as dateIn,\n" +
            "    DATE_FORMAT(date_out, '%d/%m/%Y') as dateOut,\n" +
            "    total_rooms as totalRooms,\n" +
            "    total_price as totalPrice,\n" +
            "    bookings.id\n" +
            "FROM\n" +
            "    `bookings`\n" +
            "JOIN booking_checkin_checkouts ON booking_checkin_checkouts.id_booking = bookings.id\n" +
            "JOIN type_rooms ON type_rooms.id = booking_checkin_checkouts.id_type_room\n" +
            "JOIN hotels ON hotels.id = bookings.id_hotel\n" +
            "WHERE\n" +
            "    bookings.id = ?1 AND bookings.status = \"Đã đặt cọc\";", nativeQuery = true)
    Optional<GuestBookingResponse> guestBookingResponse(Long idBooking);
}
