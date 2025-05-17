package com.test.test.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteTreatmentDto {
    @NotNull(message = "نمیتواند خالی باشد")
    Integer id;
}
