package com.example.assetrecordingapp.dto;

import com.example.assetrecordingapp.constant.ErrorCodeEnum;
import lombok.Data;

@Data
public class HttpResponse<T> {
    private int code;
    private String msg;
    private Long tms;
    private T data;

    public static <T> HttpResponse<T> success(T data) {
        HttpResponse<T> httpResponse = new HttpResponse<>();
        httpResponse.setCode(ErrorCodeEnum.SUCCESS.getCode());
        httpResponse.setMsg(ErrorCodeEnum.SUCCESS.getDesc());
        httpResponse.setData(data);
        httpResponse.setTms(System.currentTimeMillis());
        return httpResponse;
    }

    public static HttpResponse<Void> success() {
        HttpResponse<Void> httpResponse = new HttpResponse<>();
        httpResponse.setCode(ErrorCodeEnum.SUCCESS.getCode());
        httpResponse.setMsg(ErrorCodeEnum.SUCCESS.getDesc());
        httpResponse.setTms(System.currentTimeMillis());
        return httpResponse;
    }

    public static HttpResponse<Void> fail(ErrorCodeEnum errorCode) {
        HttpResponse<Void> httpResponse = new HttpResponse<>();
        httpResponse.setCode(errorCode.getCode());
        httpResponse.setMsg(errorCode.getDesc());
        httpResponse.setTms(System.currentTimeMillis());
        return httpResponse;
    }

}
