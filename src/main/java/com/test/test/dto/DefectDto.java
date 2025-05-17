package com.test.test.dto;

import com.test.test.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefectDto {
    private String text;
    private Status statusText;
    private LocalDateTime createdAt;
}
