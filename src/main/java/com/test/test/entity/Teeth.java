package com.test.test.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Teeth {
    nervousness("نیاز به عصب کشی دارد"),
    to_fill("نیاز به پر کردن دارد"),
    implant("نیاز به ایمپلنت دارد");


    private final String value;

    Teeth(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue(){
        return value;
    }


}
