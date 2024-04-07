package com.alipay.authserver.interceptor;

import com.alipay.authcommon.constants.Constants;
import com.alipay.authcommon.utils.JsonUtils;
import com.alipay.authserver.domain.User;
import com.alipay.authserver.enums.ErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
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
        HttpSession session = request.getSession();

        //获取session中存储的token
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        //TODO: 如果不存在user则需要跳转到登陆页面
        if (Objects.isNull(user)) {
            log.error("session do not exist user");
            return generateErrorResp(response, ErrorCodeEnum.INVALID_GRANT);
        }

        return true;
    }

    private boolean generateErrorResp(HttpServletResponse response, ErrorCodeEnum errorCodeEnum) throws Exception {

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");

        Map<String, String> result = new HashMap<>(2);
        result.put("error", errorCodeEnum.getError());
        result.put("error_description", errorCodeEnum.getErrorDescription());

        response.getWriter().write(JsonUtils.toJson(result));

        return false;
    }
}