package com.fantechs.provider.baseapi.esop.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Created by dell on 2017/9/25.
 *
 * 设置默认用户名+密码
 */
public class BasicAuthenticator extends Authenticator {
    String userName;
    String password;
    public BasicAuthenticator(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password.toCharArray());
    }
}
