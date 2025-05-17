package com.test.test.dto;

import com.test.test.entity.Teeth;
import com.test.test.entity.TeethStatus;
import lombok.Data;

import java.util.List;

@Data
public class SecDto {
    Teeth teeth;
    int numberOfteeth;
    public SecDto(Teeth teeth, int numberOfteeth) {
        this.teeth = teeth;
        this.numberOfteeth = numberOfteeth;
    }


}
