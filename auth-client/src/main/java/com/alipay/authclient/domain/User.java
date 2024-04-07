package com.alipay.authclient.domain;

import java.util.Date;
import lombok.Data;

@Data
public class User {
    private Integer id;

    private String username;

    private String password;

    private String mobile;

    private String email;

    private Date createTime;

    private Date updateTime;

    private Integer status;
}