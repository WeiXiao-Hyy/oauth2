package com.alipay.authclient.interceptor;

import com.alipay.authcommon.constants.Constants;
import com.alipay.authcommon.err.BizException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author hyy
 * @Description
 * @create 2024-04-07 12:44
 */

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws BizException {
        HttpSession session = request.getSession();

        //当前系统请求认证服务器之后返回的authorization code
        String code = request.getParameter("code");

        //原封不动返回的state
        String state = request.getParameter("state");

        //code不为空，则说明当前请求是从认证服务器返回的回调请求
        if (StringUtils.isNoneBlank(code)) {
            String savedState = (String) session.getAttribute(Constants.SESSION_AUTH_CODE_STATUS);
            //1. 校验状态码是否匹配
            return !StringUtils.isNoneBlank(savedState) || !StringUtils.isNoneBlank(state) || state.equalsIgnoreCase(savedState);
        }
        return true;
    }
}