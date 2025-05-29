package com.example.assetrecordingapp.constant;

import com.example.assetrecordingapp.exception.BizException;
import com.example.assetrecordingapp.util.BaseValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum implements BaseValidator {

    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数错误"),

    SYSTEM_ERROR(2, "系统错误"),

    NO_AUTH(3, "无权访问"),

    AUTH_EXPIRED(4, "授权已过期"),

    ;

    private int code;
    private String desc;

    @Override
    public RuntimeException buildException() {
        return new BizException(this);
    }

    @Override
    public RuntimeException buildException(String message) {
        return new BizException(this, message);
    }
}
