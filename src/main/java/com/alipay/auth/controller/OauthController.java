package com.alipay.auth.controller;

import com.alipay.auth.common.res.ResultResponse;
import com.alipay.auth.service.AuthorizationService;
import com.alipay.auth.service.req.AuthClientAuthorizeReq;
import com.alipay.auth.service.req.AuthClientRegisterReq;
import com.alipay.auth.service.req.AuthClientTokenReq;
import com.alipay.auth.service.res.AuthClientTokenResp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hyy
 * @Description
 * @create 2024-04-01 10:52
 */

@Controller
@RequestMapping("/oauth2.0")
public class OauthController {

    @Resource(name = "authorizationServiceImpl")
    private AuthorizationService authorizationService;

    @PostMapping(value = "/clientRegister", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultResponse<Boolean> clientRegister(@RequestBody AuthClientRegisterReq req) {
        boolean res = authorizationService.register(req);
        return ResultResponse.success(res);
    }

    @RequestMapping("/authorize")
    public ResultResponse<String> authorize(@RequestBody AuthClientAuthorizeReq request) {
        String uri = authorizationService.authorize(request);
        return ResultResponse.success(uri);
    }

    @RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultResponse<AuthClientTokenResp> token(@RequestBody AuthClientTokenReq req) {
        AuthClientTokenResp res = authorizationService.token(req);
        return ResultResponse.success(res);
    }
}