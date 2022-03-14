package com.fpt.hotel.service;

import com.fpt.hotel.model.User;

import javax.mail.MessagingException;

public interface ForgotPasswordService {

     User findByEmail(String email) throws MessagingException;
}
