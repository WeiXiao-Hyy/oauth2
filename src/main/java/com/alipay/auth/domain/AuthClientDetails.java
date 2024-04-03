package com.alipay.auth.domain;

import io.micrometer.common.util.StringUtils;
import lombok.Builder;
import lombok.Data;

/**
 * @author hyy
 * @Description
 * @create 2024-04-01 11:53
 */

@Data
@Builder
public class AuthClientDetails {
    private Integer id;

    private String clientId;

    private String clientName;

    private String clientSecret;

    private String callbackUrl;

    public boolean checkRegisterParamValid() {
        return StringUtils.isNotBlank(this.callbackUrl)
                && StringUtils.isNotBlank(this.clientName);
    }
}