package com.efub.gogildong.email.service;

public interface EmailSender {

    void send(String to, String subject, String content);
}