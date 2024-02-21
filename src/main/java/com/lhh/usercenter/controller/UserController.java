package com.lhh.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhh.usercenter.constant.UserConstant;
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
    public Long userRegister(@RequestBody UserRegisterDTO userRegisterDTO){
        if (userRegisterDTO==null){
            return null;
        }

        String userAccount=userRegisterDTO.getUserAccount();
        String userPassword=userRegisterDTO.getUserPassword();
        String checkPassword=userRegisterDTO.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }

        return userService.userRegister(userAccount, userPassword,checkPassword );
    }


    /**
     * 登录接口
     * @param userLoginDTO
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest httpServletRequest){
        if (userLoginDTO==null){
            return null;
        }

        String userAccount=userLoginDTO.getUserAccount();
        String userPassword=userLoginDTO.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }

        return userService.userLogin(userAccount,userPassword,httpServletRequest);
    }


    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request){
        Object o = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user=(User) o;
        if (user==null){
            return null;
        }
        //TODO 校验用户是否合法
        long userId=user.getId();
        user=userService.getById(userId);
        log.info("请求当前用户，{}",user);
        return userService.safeUser(user);
    }


    /**
     * 查询用户
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request ){
        // 鉴定权限
        if(!isAdmin(request)){
            return new ArrayList<>();
        }

        QueryWrapper queryWrapper=new QueryWrapper();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }


        List<User> list = userService.list(queryWrapper);

        List<User> safeList = list.stream()
                .map(user -> userService.safeUser(user))
                .toList();

        return safeList;
    }


    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable long id,HttpServletRequest request){
        //鉴定权限
        if (!isAdmin(request)){
            return false;
        }

        if (id<=0) return false;
        return userService.removeById(id);
    }


    private boolean isAdmin(HttpServletRequest request){
        // 鉴定权限
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user=(User)userObj;
        return  user!=null && Objects.equals(user.getUserRole(), UserConstant.ADMIN_ROLE);
    }




}
