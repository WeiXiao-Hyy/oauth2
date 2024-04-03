package com.alipay.auth.service.auth.impl;

import com.alipay.auth.service.auth.AuthorizationService;

import com.alipay.auth.service.auth.req.AuthClientRegisterReq;
import com.alipay.auth.utils.EncryptUtils;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @Description
 * @create 2024-04-01 12:40
 */
@Service("authorizationServiceImpl")
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public boolean register(AuthClientRegisterReq request) {
        //客户端的名称为回调地址不能为空
        if (!request.checkParam()) {
            return false;
        }
        //生成24位随机的clientId TODO: 保证唯一性
        String clientId = EncryptUtils.getRandomStr1(20);

        //生成32位随机的clientSecret
        String clientSecret = EncryptUtils.getRandomStr1(32);



    }
}