package com.fantechs.common.base.general.dto.restapi;

import lombok.Data;

import java.io.Serializable;

@Data
public class RestapiChkLogUserInfoApiDto implements Serializable {

    protected String proCode;  //线别编码
    protected String processCode;   //工序编码
    protected String userCode; //登录用户
    protected String password; //登录密码

}
