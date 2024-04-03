package com.alipay.auth.service.auth.impl;

import com.alipay.auth.dao.mapper.AuthClientDetailsMapper;
import com.alipay.auth.domain.AuthClientDetails;
import com.alipay.auth.service.auth.AuthorizationService;

import com.alipay.auth.service.auth.req.AuthClientRegisterReq;
import com.alipay.auth.utils.EncryptUtils;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @Description
 * @create 2024-04-01 12:40
 */
@Service("authorizationServiceImpl")
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private AuthClientDetailsMapper authClientDetailsMapper;

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

        Date current = new Date();

        //保存注册信息
        AuthClientDetails authClientDetails = AuthClientDetails.builder()
                .clientId(clientId)
                .clientName(request.getName())
                .clientSecret(clientSecret)
                .redirectUri(request.getRedirectUri())
                .description(request.getDescription())
                .createTime(current)
                .updateTime(current)
                .build();
        int res = authClientDetailsMapper.insertSelective(authClientDetails);
        return res > 0;
    }
}