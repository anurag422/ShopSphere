package com.shopsphere.auth.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private LocalDateTime timeStamp;

}
