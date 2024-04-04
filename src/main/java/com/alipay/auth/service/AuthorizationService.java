package com.alipay.auth.service;


import com.alipay.auth.service.req.AuthClientAuthorizeReq;
import com.alipay.auth.service.req.AuthClientRegisterReq;
import com.alipay.auth.service.req.AuthClientTokenReq;
import com.alipay.auth.service.res.AuthClientTokenResp;

/**
 * @author hyy
 * @Description
 * @create 2024-04-01 11:52
 */
public interface AuthorizationService {

    /**
     * 注册需要接入的客户端信息
     *
     * @param request {@link AuthClientRegisterReq } 用户传入的关键信息
     * @return boolean
     */
    boolean register(AuthClientRegisterReq request);

    /**
     * 根据clientId和scope以及当前时间戳生成AuthorizationCode(有效期为10分钟)
     *
     * @param request {@link AuthClientAuthorizeReq}
     * @return authorize_code
     */
    String authorize(AuthClientAuthorizeReq request);

    /**
     * 根据authorize_code获取access_token
     *
     * @param request {@link AuthClientTokenReq}
     * @return token
     */
    AuthClientTokenResp token(AuthClientTokenReq request);
}