package com.alipay.authserver.controller;

import com.alipay.authcommon.anno.ResponseResult;
import com.alipay.authcommon.constants.Constants;
import com.alipay.authserver.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyy
 * @Description
 * @create 2024-04-04 11:49
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    @ResponseResult
    public void login(HttpServletRequest request) {
        //测试使用，session中保存登陆用户的信息
        User user = User.builder()
                .id(2)
                .username("hyy")
                .email("hjlbupt@163.com")
                .build();
        HttpSession session = request.getSession();
        session.setAttribute(Constants.SESSION_USER, user);
    }
}