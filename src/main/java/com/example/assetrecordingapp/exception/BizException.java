package com.example.assetrecordingapp.exception;

import com.example.assetrecordingapp.constant.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    private ErrorCodeEnum errorCode;

    public BizException(ErrorCodeEnum errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

    public BizException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
