package com.alipay.authserver.service.req;

import lombok.Data;
import lombok.NonNull;

/**
 * @author hyy
 * @Description
 * @create 2024-04-08 00:44
 */
@Data
public class AuthClientAgreeReq {

    @NonNull
    private Integer clientId;

    @NonNull
    private Integer scope;
}