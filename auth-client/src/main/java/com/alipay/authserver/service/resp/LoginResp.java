package com.alipay.authserver.service.resp;

import lombok.Builder;
import lombok.Data;

/**
 * @author hyy
 * @Description
 * @create 2024-04-06 10:01
 */
@Data
@Builder
public class LoginResp {
    private String redirectUri;

    private String token;
}