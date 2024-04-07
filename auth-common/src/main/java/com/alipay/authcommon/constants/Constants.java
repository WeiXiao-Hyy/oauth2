package com.alipay.authcommon.constants;

/**
 * @author hyy
 * @Description
 * @create 2024-04-03 21:52
 */
public class Constants {
    /**
     * 用户信息在session中存储的变量名
     */
    public static final String SESSION_USER = "SESSION_USER";

    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 授权页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_AUTH_REDIRECT_URL = "SESSION_AUTH_REDIRECT_URL";

    /**
     * 请求Authorization Code的随机状态码在session中存储的变量名
     */
    public static final String SESSION_AUTH_CODE_STATUS = "AUTH_CODE_STATUS";

    /**
     * Access Token在session中存储的变量名
     */
    public static final String SESSION_ACCESS_TOKEN = "ACCESS_TOKEN";
}