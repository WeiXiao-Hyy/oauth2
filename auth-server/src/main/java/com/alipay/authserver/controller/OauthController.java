package com.alipay.authserver.controller;

import com.alipay.authcommon.anno.ResponseResult;
import com.alipay.authcommon.constants.Constants;
import com.alipay.authserver.service.AuthorizationService;
import com.alipay.authserver.service.req.AuthClientAuthorizeReq;
import com.alipay.authserver.service.req.AuthClientRefreshTokenReq;
import com.alipay.authserver.service.req.AuthClientRegisterReq;
import com.alipay.authserver.service.req.AuthClientTokenReq;
import com.alipay.authserver.service.res.AuthClientRefreshTokenResp;
import com.alipay.authserver.service.res.AuthClientTokenResp;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

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

    @PostMapping(value = "/agree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseResult
    public String agree(HttpServletRequest req) {
        return authorizationService.agree(req);
    }

    @RequestMapping("/authorize")
    @ResponseResult
    public String authorize(@RequestBody AuthClientAuthorizeReq request) {
        return authorizationService.authorize(request);
    }

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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