package com.test.test.dto;

import com.test.test.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ResponStatusDto  {
    Status status;
    private LocalDateTime updatedAt;
    VisitDto visitDto;
}
