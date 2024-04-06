package com.alipay.auth.service.req;

import lombok.Data;
import lombok.NonNull;

/**
 * @author hyy
 * @Description
 * @create 2024-04-03 22:59
 */
@Data
public class AuthClientTokenReq {
    //授权方式
    @NonNull
    private String grantType;

    @NonNull
    private String code;

    @NonNull
    private String clientId;

    @NonNull
    private String clientSecret;

    @NonNull
    private String redirectUri;
}