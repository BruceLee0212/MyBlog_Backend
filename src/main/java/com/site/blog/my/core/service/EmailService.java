package com.site.blog.my.core.service;

public interface EmailService {

    /**
     * send basic email to the user
     */
    void sendEmail(String fromEmail, String toEmail, String subject, String body);

    /**
     * send token email to the user
     */
    void sendTokenEmail(String toEmail, String content);
}
