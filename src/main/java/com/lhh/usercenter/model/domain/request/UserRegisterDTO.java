package com.lhh.usercenter.model.domain.request;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册DTO
 */

@Data
public class UserRegisterDTO implements Serializable {

    @Serial
    private static final long serialVersionUID=111L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
