package com.example.assetrecordingapp.exception;

import com.example.assetrecordingapp.constant.ErrorCodeEnum;
import com.example.assetrecordingapp.dto.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public HttpResponse<Void> handleBizException(BizException ex) {
        log.warn("业务异常", ex);
        return HttpResponse.fail(ex.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public HttpResponse<Void> handleGlobalException(Exception ex) {
        log.error("系统异常", ex);
        return HttpResponse.fail(ErrorCodeEnum.SYSTEM_ERROR);
    }
}
