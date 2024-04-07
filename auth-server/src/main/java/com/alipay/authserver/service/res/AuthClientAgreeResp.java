package com.alipay.authserver.service.res;

import lombok.Builder;
import lombok.Data;

/**
 * @author hyy
 * @Description
 * @create 2024-04-08 00:45
 */
@Data
@Builder
public class AuthClientAgreeResp {
    private boolean success;
}