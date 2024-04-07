package com.alipay.authserver.service.req;

import lombok.Data;
import lombok.NonNull;

/**
 * @author hyy
 * @Description
 * @create 2024-04-06 10:00
 */
@Data
public class LoginReq {
    @NonNull
    private String redirectUri;

    @NonNull
    private String code;
}