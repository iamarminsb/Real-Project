package com.test.test.dto;

import com.test.test.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AllVisitResponse {
    private String username;
    private Status statusText;
    private int visitId;
    private LocalDateTime visitDate;

    public AllVisitResponse(String username , Status statusText, int visitId, LocalDateTime visitDate) {
        this.username = username;
        this.statusText=statusText;
        this.visitId=visitId;
        this.visitDate=visitDate;
    }
}
