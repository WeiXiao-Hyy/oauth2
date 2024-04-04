package com.alipay.auth.controller;

import com.alipay.auth.common.anno.ResponseResult;
import com.alipay.auth.service.AuthorizationService;
import com.alipay.auth.service.req.AuthClientAuthorizeReq;
import com.alipay.auth.service.req.AuthClientRefreshTokenReq;
import com.alipay.auth.service.req.AuthClientRegisterReq;
import com.alipay.auth.service.req.AuthClientTokenReq;
import com.alipay.auth.service.res.AuthClientRefreshTokenResp;
import com.alipay.auth.service.res.AuthClientTokenResp;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyy
 * @Description
 * @create 2024-04-01 10:52
 */
@RestController
@RequestMapping("/oauth2.0")
public class OauthController {

    @Resource(name = "authorizationServiceImpl")
    private AuthorizationService authorizationService;

    @PostMapping(value = "/clientRegister", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseResult
    public boolean clientRegister(@RequestBody AuthClientRegisterReq req) {
        return authorizationService.register(req);
    }

    @RequestMapping("/authorize")
    @ResponseResult
    public String authorize(@RequestBody AuthClientAuthorizeReq request) {
        return authorizationService.authorize(request);
    }

    @RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseResult
    public AuthClientTokenResp token(@RequestBody AuthClientTokenReq req) {
        return authorizationService.token(req);
    }

    @RequestMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseResult
    public AuthClientRefreshTokenResp refresh(@RequestBody AuthClientRefreshTokenReq req) {
        return authorizationService.refreshToken(req);
    }
}