package com.lhh.usercenter.common;

import com.lhh.usercenter.constant.CodeConstant;
import com.sun.net.httpserver.Authenticator;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {


    private Boolean success;
    //报错代号
    //也作为如何处理错误的指示
    private Integer errorCode;
    //错误信息
    private String msg;
    //数据
    private T data;

    /**
     * 成功，无返回data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.success = true;
        result.errorCode=CodeConstant.SUCCESS;
        return result;
    }


    /**
     * 成功，返回data
     * @param object
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.success=true;
        result.errorCode = CodeConstant.SUCCESS;
        return result;
    }


    /**
     * 返回错误结果，默认处理方式为显示错误信息
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.success=false;
        result.errorCode = CodeConstant.ERROR_MESSAGE;
        return result;
    }


    /**
     * 自定义处理方式，通过指定 code 来指定前端如何处理
     * @param msg
     * @param code
     * @return
     * @param <T>
     */
    public static <T> Result<T> error(String msg,Integer code) {
        Result result = new Result();
        result.msg = msg;
        result.success=false;
        result.errorCode = code;
        return result;
    }
}