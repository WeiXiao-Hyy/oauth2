package com.alipay.auth.service.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author hyy
 * @Description
 * @create 2024-04-04 14:52
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthClientRefreshTokenReq {

    @NonNull
    private String refreshToken;
}