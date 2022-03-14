package com.fpt.hotel.user.dto.response;
import com.fpt.hotel.model.User;
import lombok.Data;

@Data
public class CommentResponse {

    private Long id;
    private String comment;
    private double star;
    private Integer user_id;
    private String username;
}
