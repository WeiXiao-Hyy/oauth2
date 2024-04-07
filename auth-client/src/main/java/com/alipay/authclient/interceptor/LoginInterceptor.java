package com.alipay.authclient.interceptor;

import com.alipay.authcommon.constants.Constants;
import com.alipay.authcommon.utils.SpringContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author hyy
 * @Description
 * @create 2024-04-07 16:20
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //获取session中存储的token
        String accessToken = (String) session.getAttribute(Constants.SESSION_ACCESS_TOKEN);

        if (StringUtils.isNoneBlank(accessToken)) {
            return true;
        } else {
//            response.sendRedirect(request.getContextPath() + "/login?redirectUrl=" + SpringContextUtils.getRequestUrl(request));
            log.warn("Login interceptor handled redirect url to login uri");
            return false;
        }
    }
}