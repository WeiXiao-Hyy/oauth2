package com.alipay.authserver.service;

import com.alipay.authserver.service.resp.LoginResp;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author hyy
 * @Description
 * @create 2024-04-06 09:59
 */
public interface LoginService {

    LoginResp login(HttpServletRequest request);
}