package com.alipay.auth.service.res;

import lombok.Builder;
import lombok.Data;

/**
 * @author hyy
 * @Description
 * @create 2024-04-04 10:42
 */

@Data
@Builder
public class AuthClientTokenResp {

    private String accessToken;

    private String refreshToken;

    private Long expiresIn;

    private String scope;
}