package com.example.assetrecordingapp.aop;

import com.example.assetrecordingapp.annotation.RequireLogin;
import com.example.assetrecordingapp.constant.ErrorCodeEnum;
import com.example.assetrecordingapp.context.UserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.example.assetrecordingapp.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class AuthAspect {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Around("@annotation(com.example.assetrecordingapp.annotation.RequireLogin)")
    public Object checkJwt(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ErrorCodeEnum.SYSTEM_ERROR.isNotNull(request);

        String token = request.getHeader("Authorization");

        ErrorCodeEnum.NO_AUTH.isNotNull(token, "未登录");
        ErrorCodeEnum.NO_AUTH.isTrue(token.startsWith("Bearer "), "未登录");

        token = token.substring(7);
        ErrorCodeEnum.NO_AUTH.isTrue(jwtTokenUtil.validateToken(token), "未登录");
        ErrorCodeEnum.AUTH_EXPIRED.isTrue(!jwtTokenUtil.isTokenExpired(token), "授权已过期");


        UserContext.setCurrentUserId(jwtTokenUtil.getUserIdFromToken(token));
        
        try {
            return joinPoint.proceed();
        } finally {
            UserContext.clear();
        }
    }
}
