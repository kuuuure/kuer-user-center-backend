package com.lhh.usercenter.model.domain.request;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录DTO
 */

@Data
public class UserLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID=111L;

    private String userAccount;

    private String userPassword;

}
