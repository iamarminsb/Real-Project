package com.test.test.dto;

import com.test.test.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RegisterRequest {
    @NotBlank(message = "نام کاربری نا معتبر است")
    private String username;
    @NotBlank(message ="نام کاربری نا معتبر است" )
    private String password;
    private String phone;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;

    }
}
