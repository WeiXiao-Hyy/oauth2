package com.alipay.authcommon.err;

/**
 * @author hyy
 * @Description
 * @create 2024-02-14 12:24
 */
public interface BaseErrorInfoInterface {

    /**
     * 错误码
     *
     * @return
     */
    String getResultCode();

    /**
     * 错误描述
     *
     * @return
     */
    String getResultMsg();
}