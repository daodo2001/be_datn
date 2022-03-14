package com.fpt.hotel.service;

import com.fpt.hotel.payload.response.MailInfo;
import javax.mail.MessagingException;

public interface MailerService {
    void send(MailInfo mail) throws MessagingException;

    void send(String to, String subject, String body) throws MessagingException;

    void queue(MailInfo mail) throws MessagingException;

    void queue(String to, String subject, String body) throws MessagingException;
}

