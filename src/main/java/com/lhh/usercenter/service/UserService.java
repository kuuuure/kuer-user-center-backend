package com.lhh.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhh.usercenter.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author LHH
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-02-19 21:33:35
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);


    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户脱敏
     * @param user
     * @return
     */
    User safeUser(User user);


    /**
     * 用户退出登录
     * @param request
     * @return
     */
    void userLogout(HttpServletRequest request);
}
