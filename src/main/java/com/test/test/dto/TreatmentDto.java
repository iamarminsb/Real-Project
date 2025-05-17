package com.test.test.dto;

import com.test.test.entity.Teeth;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TreatmentDto {
    @NotNull(message = "تعداد دندان‌ها الزامی است.")
    @Min(value = 1, message = "تعداد دندان‌ها باید حداقل ۱ باشد.")
    @Max(value = 32, message = "تعداد دندان‌ها نمی‌تواند بیشتر از ۳۲ باشد.")
    Integer numberofteeths;
    @NotNull(message = "نمی تواند خالی باشد")
    Teeth teeth;
    Integer idTeeth;
    String docText;

}

