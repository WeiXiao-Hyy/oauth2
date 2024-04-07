package com.alipay.authserver.service.req;

import lombok.Data;
import lombok.NonNull;

/**
 * @author hyy
 * @Description
 * @create 2024-04-03 22:56
 */

@Data
public class AuthClientAuthorizeReq {
    @NonNull
    private String clientId;

    @NonNull
    private String scope;

    @NonNull
    private String redirectUri;

    private String status;
}