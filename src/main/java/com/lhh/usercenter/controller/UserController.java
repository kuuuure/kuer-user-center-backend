package com.lhh.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhh.usercenter.common.Result;
import com.lhh.usercenter.constant.MessageConstant;
import com.lhh.usercenter.constant.UserConstant;
import com.lhh.usercenter.exception.BaseException;
import com.lhh.usercenter.model.domain.User;
import com.lhh.usercenter.model.domain.request.UserLoginDTO;
import com.lhh.usercenter.model.domain.request.UserRegisterDTO;
import com.lhh.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 用户接口
 */
// 这个注解可以让返回的数据转为json类型
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;


    /**
     * 用户注册接口
     * @param userRegisterDTO 参数
     * @return
     */
    @PostMapping("/register")
    public Result<Long> userRegister(@RequestBody UserRegisterDTO userRegisterDTO){
        if (userRegisterDTO==null){
            throw new BaseException(MessageConstant.PARA_ERROR);
        }

        String userAccount=userRegisterDTO.getUserAccount();
        String userPassword=userRegisterDTO.getUserPassword();
        String checkPassword=userRegisterDTO.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BaseException(MessageConstant.PARA_ERROR);
        }

        long l = userService.userRegister(userAccount, userPassword, checkPassword);
        return Result.success(l);
    }


    /**
     * 登录接口
     * @param userLoginDTO
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public Result<User> userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest httpServletRequest){
        if (userLoginDTO==null){
            throw new BaseException(MessageConstant.PARA_ERROR);
        }

        String userAccount=userLoginDTO.getUserAccount();
        String userPassword=userLoginDTO.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BaseException(MessageConstant.PARA_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return Result.success(user);
    }


    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request){
        Object o = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user=(User) o;
        if (user==null){
            throw new BaseException(MessageConstant.NO_LOGIN);
        }
        //TODO 校验用户是否合法
        long userId=user.getId();
        user=userService.getById(userId);
        log.info("请求当前用户，{}",user);
        User safedUser = userService.safeUser(user);
        return Result.success(safedUser);
    }


    /**
     * 查询用户
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public Result<List<User>> searchUsers(String username,HttpServletRequest request ){

        log.info("请求查询用户列表");

        // 鉴定权限
        if(!isAdmin(request)){
            log.info("该用户无权查询");
            throw new BaseException(MessageConstant.NO_AUTH);
        }

        QueryWrapper queryWrapper=new QueryWrapper();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }


        List<User> list = userService.list(queryWrapper);

        List<User> safeList = list.stream()
                .map(user -> userService.safeUser(user))
                .toList();

        return Result.success(safeList);
    }


    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@PathVariable long id,HttpServletRequest request){
        //鉴定权限
        if (!isAdmin(request)){
            log.info("该用户无权删除");
            throw new BaseException(MessageConstant.NO_AUTH);
        }

        if (id<=0) return Result.success(false);
        boolean b = userService.removeById(id);
        return Result.success(b);
    }


    private boolean isAdmin(HttpServletRequest request){
        // 鉴定权限
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user=(User)userObj;
        return  user!=null && Objects.equals(user.getUserRole(), UserConstant.ADMIN_ROLE);
    }


    /**
     * 用户退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result userLogout(HttpServletRequest request){
        userService.userLogout(request);
        return Result.success();
    }


}
