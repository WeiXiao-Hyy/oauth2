package com.alipay.authclient.service.req;

import lombok.Builder;
import lombok.Data;

/**
 * @author hyy
 * @Description
 * @create 2024-04-07 11:30
 */
@Data
@Builder
public class RequestTokenDTO {

    private String clientId;

    private String clientSecret;

    private String code;

    private String redirectUri;

    private String grantType;
}