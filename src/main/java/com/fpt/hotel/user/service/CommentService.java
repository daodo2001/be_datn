package com.fpt.hotel.user.service;
import com.fpt.hotel.model.Comment;
import com.fpt.hotel.user.dto.request.CommentRequest;
import com.fpt.hotel.user.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse create(CommentRequest commentRequest);

    List<Comment> findAllByHotels(Long id);

    List<Integer> checkOrderBookingComments(Integer id_user, Long id_hotel);

    Double avgStar(Long id);

    List<Integer> checkUserComments(Integer user_id, Long hotel_id);

}