package com.shopshpere.user.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ErrorResponse {
    private LocalDateTime timeStamp;

    private String error;

    private int status;

    private String message;

    private String path;


}
