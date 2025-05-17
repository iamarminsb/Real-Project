package com.test.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String errorId;     // کدی که از فایل properties خونده میشه
    private String message;
    private String traceId;
    private LocalDateTime timestamp;
    public ErrorResponse(int status, String errorId, String message) {
        this.status = status;
        this.errorId = errorId;
        this.message = message;
        this.traceId = UUID.randomUUID().toString();      // تولید Trace ID تصادفی
        this.timestamp = LocalDateTime.now();

    }
}







