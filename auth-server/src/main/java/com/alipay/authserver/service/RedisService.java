package com.alipay.authserver.service;

import java.util.concurrent.TimeUnit;

/**
 * File Description.
 *
 * @author arron
 * @date crated at 2024/4/3 21:26
 * @see com.alipay.authserver.service
 */
public interface RedisService {
    void set(String key, Object value);

    void setWithExpire(String key, Object value, long time, TimeUnit timeUnit);

    <K> K get(String key);

    boolean delete(String key);
}
