package com.alipay.authserver.interceptor;

import com.alipay.authcommon.enums.ErrorCodeEnum;
import com.alipay.authcommon.err.BizException;
import com.alipay.authcommon.utils.DateUtils;
import com.alipay.authserver.dao.mapper.AuthAccessTokenMapper;
import com.alipay.authserver.domain.AuthAccessToken;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author hyy
 * @Description
 * @create 2024-04-04 15:26
 */
public class AuthAccessTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthAccessTokenMapper authAccessTokenMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws BizException {
        String accessToken = request.getParameter("access_token");
        if (StringUtils.isBlank(accessToken)) {
            throw new BizException(ErrorCodeEnum.EXPIRED_TOKEN.getError());
        }

        //查询数据库中的access_token
        AuthAccessToken authAccessToken = authAccessTokenMapper.selectByAccessToken(accessToken);
        if (Objects.isNull(authAccessToken)) {
            throw new BizException(ErrorCodeEnum.INVALID_GRANT.getError());
        }

        //检查是否过期
        Long savedExpiresAt = authAccessToken.getExpiresIn();

        //过期日期
        LocalDateTime expiresDateTime = DateUtils.ofEpochSecond(savedExpiresAt, null);
        //当前日期
        LocalDateTime nowDateTime = DateUtils.now();

        //如果Access_Token失效,则返回错误提示
        if (isExpired(expiresDateTime, nowDateTime)) {
            throw new BizException(ErrorCodeEnum.EXPIRED_TOKEN.getError());
        }

        return true;
    }

    private boolean isExpired(LocalDateTime expiresDateTime, LocalDateTime nowDateTime) {
        return expiresDateTime.isAfter(nowDateTime);
    }
}