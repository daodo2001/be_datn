package com.fpt.hotel.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailInfo {
    private String from;
    private String to;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String body;
    private String[] attachments;

    public MailInfo(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

}
