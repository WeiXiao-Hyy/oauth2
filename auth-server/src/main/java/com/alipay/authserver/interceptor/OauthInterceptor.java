package com.alipay.authserver.interceptor;

import com.alipay.authcommon.constants.Constants;
import com.alipay.authcommon.enums.ErrorCodeEnum;
import com.alipay.authcommon.err.BizException;
import com.alipay.authcommon.utils.SpringContextUtils;
import com.alipay.authserver.dao.mapper.AuthClientDetailsMapper;
import com.alipay.authserver.dao.mapper.AuthClientUserMapper;
import com.alipay.authserver.domain.AuthClientDetails;
import com.alipay.authserver.domain.AuthClientUser;
import com.alipay.authserver.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author hyy
 * @Description 检查用户是否已经同意授权第三方应用
 * @create 2024-04-07 17:20
 */
@Slf4j
public class OauthInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthClientUserMapper authClientUserMapper;
    @Autowired
    private AuthClientDetailsMapper authClientDetailsMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //参数信息
        String params = "?redirectUri=" + SpringContextUtils.getRequestUrl(request);
        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        //权限范围
        String scopeStr = request.getParameter("scope");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");
        //返回形式
        String responseType = request.getParameter("response_type");

        //获取session中存储的token
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        if (StringUtils.isNoneBlank(clientIdStr) && StringUtils.isNoneBlank(scopeStr) && StringUtils.isNoneBlank(redirectUri) && "code".equals(responseType)) {
            params = params + "&client_id=" + clientIdStr + "&scope=" + scopeStr;

            //1. 查询是否存在授权信息
            AuthClientDetails clientDetails = authClientDetailsMapper.selectByClientId(clientIdStr);

            if (Objects.isNull(clientDetails)) {
                throw new BizException(ErrorCodeEnum.INVALID_CLIENT.getError());
            }

            if (!clientDetails.getRedirectUri().equals(redirectUri)) {
                throw new BizException(ErrorCodeEnum.REDIRECT_URI_MISMATCH.getError());
            }

            //2. 查询用户给接入的客户端是否已经授权
            AuthClientUser clientUser = authClientUserMapper.selectByClientId(clientDetails.getId(), user.getId());

            // 如果没有授权，则跳转到授权页面
            if (Objects.isNull(clientUser)) {
                response.sendRedirect(request.getContextPath() + "/oauth2.0/authorizePage" + params);
                return false;
            }

            return true;
        } else {
            throw new BizException(ErrorCodeEnum.INVALID_REQUEST.getError());
        }
    }
}