package com.lhh.usercenter.constant;


/**
 * 用户常量
 */
public interface UserConstant {
    String SALT="lhh";

    String USER_LOGIN_STATE="userLoginState";

    // 权限
    /**
     * 管理员
     */
    Integer ADMIN_ROLE=1;
    /**
     * 普通用户
     */
    Integer COMMON_ROLE=0;
}
