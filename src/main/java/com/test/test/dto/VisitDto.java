package com.test.test.dto;

import com.test.test.entity.Status;
import lombok.Data;

@Data
public class VisitDto {
    private String daromasrafi;
    private String sabeghe;
    private String elat;
    private Status statusText;
    private String username;

    public VisitDto(String daromasrafi, String sabeghe, String elat, Status statusText, String username) {
        this.daromasrafi = daromasrafi;
        this.sabeghe = sabeghe;
        this.elat = elat;
        this.statusText = statusText;
        this.username = username;
    }

    public VisitDto(String elat, String sabeghe, String daromasrafi) {
        this.elat = elat;
        this.sabeghe = sabeghe;
        this.daromasrafi = daromasrafi;
    }
    public VisitDto() {
    }

    public VisitDto(String daromasrafi, String sabeghe, String elat, Status statusText) {
        this.daromasrafi = daromasrafi;
        this.sabeghe = sabeghe;
        this.elat = elat;
        this.statusText = statusText;
    }
}
