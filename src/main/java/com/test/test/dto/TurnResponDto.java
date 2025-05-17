package com.test.test.dto;

import com.test.test.entity.Status;
import lombok.Data;

import java.util.List;

@Data
public class TurnResponDto {
    String username;
    List<SecDto> details;
    Status status;


}
