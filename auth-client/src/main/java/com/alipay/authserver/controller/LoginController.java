package com.alipay.authserver.controller;

import com.alipay.authserver.service.LoginService;
import com.alipay.authserver.service.resp.LoginResp;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyy
 * @Description
 * @create 2024-04-06 09:48
 */
@RestController
public class LoginController {

    @Resource(name = "loginServiceImpl")
    private LoginService loginService;

    @RequestMapping("/login")
    public LoginResp login(HttpServletRequest req) {
        return loginService.login(req);
    }
}