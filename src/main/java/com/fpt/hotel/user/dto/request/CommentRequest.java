package com.fpt.hotel.user.dto.request;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentRequest {

    Integer user_id;
    String comment;
    double star;
    Long hotel_id;
}