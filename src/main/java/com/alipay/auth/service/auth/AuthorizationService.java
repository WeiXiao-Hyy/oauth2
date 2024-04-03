package com.alipay.auth.service.auth;

import com.alipay.auth.service.auth.req.AuthClientRegisterReq;

/**
 * @author hyy
 * @Description
 * @create 2024-04-01 11:52
 */
public interface AuthorizationService {

    /**
     * 注册需要接入的客户端信息
     *
     * @param request 用户传入的关键信息
     * @return boolean
     */
    boolean register(AuthClientRegisterReq request);
}