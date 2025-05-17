package com.test.test.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class DateResponDto {
    String username;
    LocalDateTime scheduledAt;

    public DateResponDto(String username, LocalDateTime scheduledAt) {
        this.username = username;
        this.scheduledAt = scheduledAt;
    }
}
