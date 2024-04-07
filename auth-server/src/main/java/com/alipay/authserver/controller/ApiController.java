package com.alipay.authserver.controller;

import com.alipay.authcommon.anno.ResponseResult;
import com.alipay.authcommon.err.BizException;
import com.alipay.authcommon.utils.JsonUtils;
import com.alipay.authserver.domain.AuthAccessToken;
import com.alipay.authserver.domain.User;
import com.alipay.authserver.enums.ErrorCodeEnum;
import com.alipay.authserver.service.AuthorizationService;
import com.alipay.authserver.service.UserService;
import jakarta.annotation.Resource;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyy
 * @Description
 * @create 2024-04-06 23:32
 */

@RestController
@RequestMapping("/api")
public class ApiController {

    @Resource(name = "authorizationServiceImpl")
    private AuthorizationService authorizationService;

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @RequestMapping(value = "/users/getInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseResult
    public String getUserInfo(HttpServletRequest request) {
        String accessToken = request.getParameter("access_token");
        //查询数据库中的Access Token
        AuthAccessToken authAccessToken = authorizationService.selectByAccessToken(accessToken);

        if (Objects.isNull(authAccessToken)) {
            throw new BizException(ErrorCodeEnum.UNKNOWN_ERROR.getError());
        }
        User user = userService.selectUserInfoByScope(authAccessToken.getUserId(), authAccessToken.getScope());
        return JsonUtils.toJson(user);
    }
}