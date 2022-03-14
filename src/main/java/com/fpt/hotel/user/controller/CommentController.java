package com.fpt.hotel.user.controller;

import com.fpt.hotel.model.Comment;
import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.user.dto.request.CommentRequest;
import com.fpt.hotel.user.dto.response.CommentResponse;
import com.fpt.hotel.user.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("api/user/comment")
@CrossOrigin("*")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllByHotels(@PathVariable("id") Long id) throws MessagingException {
        List<Comment> comment = commentService.findAllByHotels(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Hiển thị danh sách comment", comment));
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<?> avgStar(@PathVariable("id") Long id) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Hiển thị danh sách comment", commentService.avgStar(id)));
    }

    @PostMapping
    public ResponseEntity<?> create( @RequestBody CommentRequest commentRequest){
        CommentResponse comment = commentService.create(commentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Đánh giá thành công", comment));
    }


    @GetMapping("/{id_user}/{id_hotel}")
    public ResponseEntity<?> checkOrderBookingComments(@PathVariable("id_user") Integer id_user, @PathVariable("id_hotel") Long id_hotel) throws MessagingException {
        var check = commentService.checkOrderBookingComments(id_user,id_hotel);
        Integer res = 0;
        if(check.size() == 0){
            res = 0;
        }else{
            res = 1;
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Check user đã đặt phòng!",res));
    }

    @GetMapping("/check/user/comments/{id_user}/{id_hotel}")
    public ResponseEntity<?> checkUserComments(@PathVariable("id_user") Integer id_user, @PathVariable("id_hotel") Long id_hotel) throws MessagingException {
        var check = commentService.checkUserComments(id_user,id_hotel);
        Integer res = 0;
        if(check.size() == 0){
            res = 1;
        }else{
            res = 0;
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Check user đã đặt comments!",res));
    }
}
