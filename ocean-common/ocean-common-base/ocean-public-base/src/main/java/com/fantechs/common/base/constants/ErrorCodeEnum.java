package com.fantechs.common.base.constants;

/**
 * 返回结果枚举
 * @author leifengzhi
 *
 */
public enum ErrorCodeEnum {

    /**
     * Gl 99990100 error code enum.
     */
    GL99990100(9999100, "参数异常"),
    /**
     * Gl 99990401 error code enum.
     */
    GL99990401(99990401, "无访问权限"),
    /**
     * Gl 000500 error code enum.
     */
    GL99990500(500, "未知异常"),
    /**
     * Gl 000403 error code enum.
     */
    GL99990403(9999403, "无权访问"),
    /**
     * Gl 000404 error code enum.
     */
    GL9999404(9999404, "找不到指定资源"),
    /**
     * Gl 99990001 error code enum.
     */
    GL99990001(99990001, "注解使用错误"),
    /**
     * Gl 99990002 error code enum.
     */
    GL99990002(99990002, "微服务不在线,或者网络超时"),
    /**
     *
     */
    GL99990005(99990005, "QL语句执行错误"),

    /**
     *  1001 用户中心
     */

    UAC10010001(10010001, "会话超时,请刷新页面重试"),
    /**
     * Uac 10010002 error code enum.
     */
    UAC10010002(10010002, "TOKEN解析失败"),
    /**
     * Uac 10010003 error code enum.
     */
    UAC10010003(10010003, "操作频率过快, 您的帐号已被冻结"),
    /**
     * Uac 10011001 error code enum.
     */
    UAC10011001(10011001, "用户Id不能为空"),
    /**
     * Uac 10011002 error code enum.
     */
    UAC10011002(10011002, "找不到用户,loginName=%s"),
    /**
     * Uac 10011003 error code enum.
     */
    UAC10011003(10011003, "找不到用户,userId=%s"),
    /**
     * Uac 10011004 error code enum.
     */
    UAC10011004(10011004, "找不到用户,email=%s"),
    /**
     * Uac 10011006 error code enum.
     */
    UAC10011006(10012006, "手机号不能为空"),
    /**
     * Uac 10011007 error code enum.
     */
    UAC10011007(10011007, "登录名不能为空"),
    /**
     * Uac 10011008 error code enum.
     */
    UAC10011008(10011008, "新密码不能为空"),
    /**
     * Uac 10011009 error code enum.
     */
    UAC10011009(10011009, "确认密码不能为空"),
    /**
     * Uac 10011010 error code enum.
     */
    UAC10011010(10011010, "两次密码不一致"),
    /**
     * Uac 10011011 error code enum.
     */
    UAC10011011(10011011, "用户不存在, userId=%s"),
    /**
     * Uac 10011012 error code enum.
     */
    UAC10011012(10011012, "登录名已存在"),
    /**
     * Uac 10011013 error code enum.
     */
    UAC10011013(10011013, "手机号已存在"),
    /**
     * Uac 10011014 error code enum.
     */
    UAC10011014(10011014, "密码不能为空"),
    /**
     * Uac 10011016 error code enum.
     */
    UAC10011016(10011016, "用户名或密码错误"),
    /**
     * Uac 10011017 error code enum.
     */
    UAC10011017(10011017, "验证类型错误"),
    /**
     * Uac 10011018 error code enum.
     */
    UAC10011018(10011018, "邮箱不能为空"),
    /**
     * Uac 10011019 error code enum.
     */
    UAC10011019(10011019, "邮箱已存在"),
    /**
     * Uac 10011023 error code enum.
     */
    UAC10011023(10011023, "越权操作"),
    /**
     * Uac 10011024 error code enum.
     */
    UAC10011024(10011024, "找不到绑定的用户, userId=%"),
    /**
     * Uac 10011025 error code enum.
     */
    UAC10011025(10011025, "用户已存在, loginName=%"),
    /**
     * Uac 10011026 error code enum.
     */
    UAC10011026(10011026, "更新用户失败, userId=%"),
    /**
     * Uac 10011027 error code enum.
     */
    UAC10011027(10011027, "找不到用户,mobile=%s"),
    /**
     * Uac 10011028 error code enum.
     */
    UAC10011028(10011028, "链接已失效"),
    /**
     * Uac 10011029 error code enum.
     */
    UAC10011029(10011029, "重置密码失败"),
    /**
     * Uac 10011031 error code enum.
     */
    UAC10011031(10011031, "验证码超时, 请重新发送验证码"),
    /**
     * Uac 10011032 error code enum.
     */
    UAC10011032(10011032, "邮箱不存在, loginName=%s,email=%s"),
    /**
     * Uac 10011033 error code enum.
     */
    UAC10011033(10011033, "清空该用户常用菜单失败"),
    /**
     * Uac 10011034 error code enum.
     */
    UAC10011034(10011034, "不允许操作admin用户"),
    /**
     * Uac 10011035 error code enum.
     */
    UAC10011035(10011035, "原始密码输入错误"),
    /**
     * Uac 10011036 error code enum.
     */
    UAC10011036(10011036, "新密码和原始密码不能相同"),
    /**
     * Uac 10011037 error code enum.
     */
    UAC10011037(10011037, "修改用户失败,userId=%s"),
    /**
     * Uac 10011038 error code enum.
     */
    UAC10011038(10011038, "激活用户失败,userId=%s"),
    /**
     * Uac 10011039 error code enum.
     */
    UAC10011039(10011039, "验证token失败"),
    /**
     * Uac 10011040 error code enum.
     */
    UAC10011040(10011040, "解析header失败"),
    /**
     * Uac 10011041 error code enum.
     */
    UAC10011041(10011041, "页面已过期,请重新登录"),

    /**
     * Uac 10012001 error code enum.
     */
    UAC10012001(10012001, "角色ID不能为空"),
    /**
     * Uac 10012002 error code enum.
     */
    UAC10012002(10012002, "拥有的角色不允许禁用"),
    /**
     * Uac 10012003 error code enum.
     */
    UAC10012003(10012003, "系统角色不能删除"),

    /**
     * Uac 10012004 error code enum.
     */
    UAC10012004(10012004, "超级角色Id不能为空"),

    /**
     * Uac 10012005 error code enum.
     */
    UAC10012005(10012005, "找不到角色信息,roleId=%s"),
    /**
     * Uac 10012006 error code enum.
     */
    UAC10012006(10012006, "删除角色失败, roleId=%s"),
    /**
     * Uac 10012007 error code enum.
     */
    UAC10012007(10012007, "批量删除角色失败, roleId=%s"),
    /**
     * Uac 10012008 error code enum.
     */
    UAC10012008(10012008, "找不到绑定的角色, roleId=%s"),

    /**
     * Uac 10012008 error code enum.
     */
    UAC10012009(10012009, "未激活用户, loginName=%s"),



    /**
     * operate 10012005 error code enum.
     * 删除失败
     */
    OPT20012000(20012000,"删除数据失败, id=%s不存在"),

    OPT20012001(20012001,"编码重复"),

    OPT20012002(20012002,"未知错误"),

    OPT20012003(20012003,"数据不存在"),

    OPT20012004(20012004,"数据被引用，不能删除"),
    ;

    private Integer code;

    private String msg;

    ErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
