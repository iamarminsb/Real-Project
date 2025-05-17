package com.test.test.dto;

import com.test.test.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitResponDto {
    private String username;
    private String daromasrafi;
    private String sabeghe;
    private String elat;
    private Status statusText;
    private LocalDateTime updatedAt;
    private List<SecDto> secDtos;
    private String secText;
    private String docText;
    private String defectText;
    private LocalDateTime scheduledAt;



}
