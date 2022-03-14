package com.fpt.hotel.user.impl;

import com.fpt.hotel.model.Comment;
import com.fpt.hotel.repository.BlackListRepository;
import com.fpt.hotel.repository.CommentRepository;
import com.fpt.hotel.repository.HotelRepository;
import com.fpt.hotel.repository.UserRepository;
import com.fpt.hotel.user.dto.request.CommentRequest;
import com.fpt.hotel.user.dto.response.CommentResponse;
import com.fpt.hotel.user.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BlackListRepository blackListRepository;


    @Override
    public CommentResponse create(CommentRequest commentRequest) {

        Comment comments = new Comment();
        List<String> blackList = new ArrayList<String>();
        blackListRepository.findAll().forEach(f -> blackList.add(f.getName()));
        List<String> tempList = new ArrayList<>();
        tempList.add(commentRequest.getComment());

        String oldStr="";
        for (int i = 0; i < blackList.size(); i++) {
            oldStr = tempList.get(i).replace(blackList.get(i), "***");
            tempList.add(oldStr);
        }
        comments.setComment(oldStr);
        comments.setStar(commentRequest.getStar());
        comments.setUsers(userRepository.findById(commentRequest.getUser_id()).get());
        comments.setHotels(hotelRepository.findById(commentRequest.getHotel_id()).get());

        CommentResponse response = new CommentResponse();
        comments = commentRepository.save(comments);
        response.setUser_id(comments.getUsers().getId());
        response.setComment(comments.getComment());
        response.setStar(comments.getStar());
        response.setId(comments.getId());

        return response;
    }

    @Override
    public List<Comment> findAllByHotels(Long id) {
        return commentRepository.findAllByHotels(id);
    }

    @Override
    public List<Integer> checkOrderBookingComments(Integer id_user, Long id_hotel) {
        return commentRepository.checkOrderBookingComments(id_user, id_hotel);
    }

    @Override
    public Double avgStar(Long id) {
        return commentRepository.avgStar(id);
    }

    @Override
    public List<Integer> checkUserComments(Integer user_id, Long hotel_id) {
        return commentRepository.checkUserComments(user_id, hotel_id);
    }


}
