package com.alipay.auth.controller;

import com.alipay.auth.common.res.ResultResponse;
import com.alipay.auth.service.auth.AuthorizationService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public ResultResponse<Boolean> clientRegister(@RequestBody(required = true) String details) {
        boolean res = authorizationService.register(details);

        return ResultResponse.success(res);
    }
}