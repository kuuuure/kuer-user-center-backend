package com.lhh.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhh.usercenter.model.domain.User;
import com.lhh.usercenter.service.UserService;
import com.lhh.usercenter.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.lhh.usercenter.constant.UserConstant.SALT;
import static com.lhh.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author LHH
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-02-19 21:33:35
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{



    @Autowired
    private UserMapper userMapper;


    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword)
    {
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        if (userAccount.length()<4){
            return -1;
        }
        if (userPassword.length()<8||checkPassword.length()<8){
            return -1;
        }



        //校验特殊字符


        //密码和校验密码相等
        if (!userPassword.equals(checkPassword)){
            return -1;
        }

        //账户不能重复
        //这里需要查询数据库，放到最后
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if (count>0){
            return -1;
        }


        //2. 加密

        String newPassword=DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

        //3. 插入数据
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        boolean result = this.save(user);
        if (!result){
            return -1;
        }
        return user.getId();
    }




    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if (userAccount.length()<4){
            return null;
        }
        if (userPassword.length()<8){
            return null;
        }

        //校验特殊字符


        //2. 加密
        String newPassword=DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",newPassword);
        User user=userMapper.selectOne(queryWrapper);

        if (user==null){
            log.info("用户账号或密码输入错误");
            return null;
        }


        //3. 用户脱敏
        User safeUser = safeUser(user);

        //3. 保存用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);


        return safeUser;
    }

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    @Override
    public User safeUser(User user){
        if (user==null){
            return null;
        }
        User safeUserInformation=new User();
        safeUserInformation.setId(user.getId());
        safeUserInformation.setUsername(user.getUsername());
        safeUserInformation.setUserAccount(user.getUserAccount());
        safeUserInformation.setAvatarUrl(user.getAvatarUrl());
        safeUserInformation.setGender(user.getGender());
        safeUserInformation.setEmail(user.getEmail());
        //safeUserInformation.setUserPassword("");
        safeUserInformation.setPhone(user.getPhone());
        safeUserInformation.setUserStatus(user.getUserStatus());
        safeUserInformation.setIsDelete(user.getIsDelete());
        safeUserInformation.setUserRole(user.getUserRole());
        return safeUserInformation;
    }

    @Override
    public int userLogout() {
        return 0;
    }
}




