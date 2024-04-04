package com.alipay.auth.service.req;

import lombok.Data;
import lombok.NonNull;

/**
 * @author hyy
 * @Description
 * @create 2024-04-04 14:52
 */

@Data
public class AuthClientRefreshTokenReq {

    @NonNull
    private String refreshToken;
}