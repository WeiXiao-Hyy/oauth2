package com.alipay.authclient.controller;

import com.alipay.authcommon.anno.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyy
 * @Description
 * @create 2024-04-07 17:11
 */
@RestController("/user")
public class UserController {

    @RequestMapping("/index")
    @ResponseResult
    public String index() {
        return "user index";
    }
}