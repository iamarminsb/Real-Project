package com.test.test.dto;

import lombok.Data;

import java.util.List;

@Data
public class SecBodyDto {
    String secText;
    List<SecDto> secList;
    String docText;
}
