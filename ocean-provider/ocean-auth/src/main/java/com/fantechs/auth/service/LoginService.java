package com.fantechs.auth.service;

import com.fantechs.common.base.response.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Create on 2022/6/1
 *
 * @author keguang_huang
 */
public interface LoginService {

    /**
     *  PC端登录
     * @param userName 用户id
     * @param password 账号密码
     * @param orgId 组织id
     * @param browserKernel 浏览器内核标识
     * @return
     */
    ResponseEntity mesLogin(String userName,String password,Long orgId,String type,String browserKernel);

    /**
     * 退出登录
     * @param request
     * @return
     */
    Boolean logout(HttpServletRequest request);
}
