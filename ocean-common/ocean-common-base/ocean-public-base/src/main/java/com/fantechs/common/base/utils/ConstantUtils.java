package com.fantechs.common.base.utils;

/**
 * @Auther: bingo.ren
 * @Date: 2020/4/21 15:54
 * @Description:
 * @Version:1.0
 */
public class ConstantUtils{
    public static final String SOURCE_SYS="SELF";//系统来源名称

    public static final Integer UNKNOWNEROOR=10000;//未知错误

    public static final Integer PARAMETER_ERROR=10001;//参数错误

    public static final Integer SYS_REMOTESERVICE_ERROR=10003;//远程服务错误

    public static final Integer SYS_IP_ERROR=10004;//IP限制不能请求该资源

    public static final Integer SYS_BUSY=10005; //系统繁忙
    public static final Integer SYS_JOB_EXPIRED=10006; //任务超时

    public static final Integer SYS_SQL_EXECUTE_ERROR=10007; //SQL语句执行错误

    public static final Integer SYS_SQL_TRANSACTION_ERROR=10008; //数据库事务执行错误
    public static final Integer SYS_DATA_NOT_EXISI=10009;//数据不存在

    public static final Integer SYS_PERMISSION_DENIED=10099;//权限不足
    public static final Integer SYS_CODE_REPEAT=10100;//编码重复

    public static final Integer USER_ACCOUNT_PASSWORD_ERROR=20100; //账号或密码错误

    public static final Integer USER_NOT_EXIST=20101; //用户不存在

    public static final Integer USER_ALREADY_EXIST=20102; //用户已存在

    public static final Integer USER_TOKEN_ERROR=20103; //token验证无效

    public static final Integer USER_TOKEN_EXPIRED=20104; //token已过期

    public static final Integer USER_TOKEN_REPLACE=20105; //token置换失败

    public static final Integer USER_AUTHCODE_ERROR=20106; //验证码错误或失效

    public static final Integer USER_PRIVILEGE_ERROR=20107;//权限错误




}
