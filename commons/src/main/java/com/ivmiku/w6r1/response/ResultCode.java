package com.ivmiku.w6r1.response;

import lombok.Getter;

@Getter
public enum ResultCode {
    OK(200, "success"),
    ERROR(-1, "error");

    private int code;
    private  String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
