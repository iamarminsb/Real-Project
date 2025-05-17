package com.test.test.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VisitCountByDayDTO {
    private LocalDate date;
    private long count;

    public VisitCountByDayDTO(LocalDate date, long count) {
        this.date = date;
        this.count = count;
    }
}
