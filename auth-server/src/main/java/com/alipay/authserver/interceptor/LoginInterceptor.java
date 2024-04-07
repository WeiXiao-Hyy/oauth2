package com.alipay.authserver.interceptor;

import com.alipay.authcommon.constants.Constants;
import com.alipay.authserver.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author hyy
 * @Description
 * @create 2024-04-04 15:55
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取session中存储的token
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //TODO: 如果不存在user则需要跳转到登陆页面
        if (Objects.isNull(user)) {
            log.error("Session not exist user");
            return false;
        }

        log.info("Get session user={}", user);
        return true;
    }
}