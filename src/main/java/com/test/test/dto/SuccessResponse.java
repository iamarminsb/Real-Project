package com.test.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse<T> {
    private String message;
    private T data;

    public SuccessResponse(T data) {
        this.message = "عملیات موفقیت‌آمیز بود";
        this.data = data;
    }
}
