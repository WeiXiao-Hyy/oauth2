package com.alipay.auth.service.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hyy
 * @Description
 * @create 2024-04-04 14:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthClientRefreshTokenResp {
    private String accessToken;

    private String refreshToken;

    private Long expiresIn;

    private String scope;
}