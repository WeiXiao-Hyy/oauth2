package com.alipay.auth.service.auth.req;

import io.micrometer.common.util.StringUtils;
import lombok.Data;

/**
 * @author hyy
 * @Description
 * @create 2024-04-03 14:28
 */

@Data
public class AuthClientRegisterReq {
    private String name;

    private String redirectUri;

    private String description;

    public boolean checkParam() {
        return StringUtils.isNotBlank(this.redirectUri)
                && StringUtils.isNotBlank(this.name);
    }
}