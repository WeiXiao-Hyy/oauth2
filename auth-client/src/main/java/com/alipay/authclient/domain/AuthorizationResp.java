package com.alipay.authclient.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author hyy
 * @Description
 * @create 2024-04-06 22:48
 */
@Data
@Builder
public class AuthorizationResp {
    /**
     * 要获取的Access Token（30天的有效期）
     */
    private String access_token;

    /**
     * 用于刷新Access Token 的 Refresh Token（10年的有效期）
     */
    private String refresh_token;

    /**
     * Access Token最终的访问范围
     */
    private String scope;

    /**
     * Access Token的有效期，以秒为单位（30天的有效期）
     */
    private Long expires_in;

    /**
     * 基于http调用Open API时所需要的Session Key，其有效期与Access Token一致
     */
    private String session_key;

    /**
     * 基于http调用Open API时计算参数签名用的签名密钥
     */
    private String session_secret;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 错误描述
     */
    private String error_description;
}