package com.alipay.authserver.service;

import com.alipay.authserver.domain.User;

/**
 * File Description.
 *
 * @author arron
 * @date crated at 2024/4/6 23:37
 * @see com.alipay.authserver.service
 */
public interface UserService {

    /**
     * 根据userId, scope查询用户
     *
     * @param userId 用户Id
     * @param scope  范围
     * @return user {@link User}
     */
    User selectUserInfoByScope(Integer userId, String scope);
}
