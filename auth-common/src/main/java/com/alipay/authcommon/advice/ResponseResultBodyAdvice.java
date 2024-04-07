package com.alipay.authcommon.advice;

import com.alipay.authcommon.anno.ResponseResult;
import com.alipay.authcommon.res.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author hyy
 * @Description
 * @create 2024-02-14 15:05
 */
@RestControllerAdvice
@Slf4j
public class ResponseResultBodyAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    private static final Class<? extends Annotation> ANNOTATION_TYPE = ResponseResult.class;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) || returnType.hasMethodAnnotation(ANNOTATION_TYPE);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 如果返回类型是string，那么springmvc是直接返回的，此时需要手动转化为json
        Class<?> returnClass = returnType.getMethod().getReturnType();
        if (body instanceof String || Objects.equals(returnClass, String.class)) {
            return objectMapper.writeValueAsString(ResultResponse.success(body));
        }
        // 防止重复包裹的问题出现
        if (body instanceof ResultResponse) {
            return body;
        }
        return ResultResponse.success(body);
    }
}