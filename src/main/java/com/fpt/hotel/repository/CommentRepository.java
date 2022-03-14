package com.fpt.hotel.repository;

import com.fpt.hotel.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment , Long> {

    @Query(value = "SELECT * FROM comments WHERE hotel_id = ?1", nativeQuery = true)
    List<Comment> findAllByHotels(Long id_hotel);


    @Query(value = "SELECT u.id FROM users u JOIN bookings b ON\n" +
            "u.id=b.id_user \n" +
            "WHERE b.id_user = ?1 AND b.id_hotel=?2 AND b.status = 'đã trả phòng'", nativeQuery = true)
    List<Integer> checkOrderBookingComments(Integer id_user, Long id_hotel);

    @Query(value = "SELECT AVG(star) AS avg_star FROM `comments` WHERE hotel_id = ?1",nativeQuery = true)
    Double avgStar(Long id);


    @Query(value = "SELECT user_id FROM `comments` WHERE user_id = ?1 AND hotel_id = ?2", nativeQuery = true)
    List<Integer> checkUserComments(Integer user_id, Long hotel_id);
}
