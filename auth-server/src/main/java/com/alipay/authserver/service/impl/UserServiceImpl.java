package com.alipay.authserver.service.impl;

import com.alipay.authcommon.enums.ScopeEnum;
import com.alipay.authserver.dao.mapper.UserMapper;
import com.alipay.authserver.domain.User;
import com.alipay.authserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @Description
 * @create 2024-04-06 23:38
 */
@Service(value = "userServiceImpl")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectUserInfoByScope(Integer userId, String scope) {
        User user = userMapper.selectByPrimaryKey(userId);

        //如果是基础权限，则部分信息不返回
        if (ScopeEnum.BASIC.getCode().equalsIgnoreCase(scope)) {
            user.setPassword(null);
            user.setCreateTime(null);
            user.setUpdateTime(null);
            user.setStatus(null);
        }

        return user;
    }
}