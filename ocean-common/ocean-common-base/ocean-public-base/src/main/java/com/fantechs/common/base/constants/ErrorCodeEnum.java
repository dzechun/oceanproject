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
    UAC10010003(10010003, "您的帐号已被冻结"),
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

    OPT20012005(20012005,"对应ID未找到正确数据"),

    OPT20012006(20012006,"数据库执行失败"),

    OPT20012007(20012007,"未找到能导出的数据"),

    OPT20012008(20012008,"条码生成错误"),

    OPT20012009(20012009,"条码产生数量超出单据数量范围"),


    /**
     * operate 30012000 error code enum.
     * 库存类相关报错
     */
    STO30012000(30012000,"库存不足/不存在"),

    STO30012001(30012001,"库存不存在"),

    // region PDA 作业相关 error code enum

    PDA40012000(40012000, "条码不存在"),
    PDA40012001(40012001, "条码重复"),
    PDA40012002(40012002, "条码barCode=%s的过站数据不存在"),
    PDA40012003(40012003, "条码流程不正确，当前工序processCode=%s, 下一工序nextProcess=%s"),
    PDA40012004(40012004, "条码状态不正确，当前状态barcodeStatus=%s"),
    PDA40012005(40012005, "工单编码workOrderCode=%s的工单不存在"),
    PDA40012006(40012006, "工单状态已完成或已挂起"),
    PDA40012007(40012007, "工单编码workOrderCode=%s的工单投产数量大于等于工单数"),
    PDA40012008(40012008, "工艺路线不存在"),
    PDA40012009(40012009, "工艺路线中工序processId=%s不存在"),
    PDA40012010(40012010, "工序processId=%s是必过工序，不可跳过！"),
    PDA40012011(40012011, "工艺路线中工序processId=%s不存在！"),
    PDA40012012(40012012, "工序processId=%s不存在！"),
    PDA40012013(40012013, "当前条码已关联包箱，不可扫码"),
    PDA40012014(40012014, "生成包箱码失败！产品料号materialId=%s与流程processId=%s关联的标签信息不存在"),
    PDA40012015(40012015, "包箱规格数量不能小于已扫条码数量"),
    PDA40012016(40012016, "当前工序产品关键物料清单未配置"),
    PDA40012017(40012017, "生成包箱码失败！条码集合barcodeRuleSetId=%s对应的条码规则关联集合信息不存在"),
    PDA40012018(40012018, "扫码所属条码工单workOrderId=%s与当前作业工单workOrderId=%s不一致"),
    PDA40012019(40012019, "扫码所属产品料号materialId=%s与当前作业工单materialId=%s不一致"),
    PDA40012020(40012020, "关键部件物料清单已满，不可扫码"),
    PDA40012021(40012021, "包箱号生成规则类别与包装规格条码规则不匹配，包箱号规则类别编码码为09"),
    PDA40012022(40012022, "获取包箱条码规则配置为空"),
    PDA40012023(40012023, "包箱条码规则详细配置没有设置"),
    PDA40012024(40012024, "产品料号materialId=%s不存在"),
    PDA40012025(40012025, "未满箱提交附件码数量必须符合工单物料清单数量"),
    PDA40012026(40012026, "产品条码参数为空，请先扫条码"),
    PDA40012027(40012027, "当前工位有未完成包箱作业，不能修改配置"),
    PDA40012028(40012028, "该附件码已绑定条码，不可重复扫描"),
    PDA40012029(40012029, "该条码部件条码已满，不可重复扫码"),
    PDA40012030(40012030, "找不到该栈板"),
    PDA40012031(40012031, "该附件码不满足工单物料清单，请重新扫码"),
    PDA40012032(40012032, "当前条码对应的工单状态异常，不允许扫码操作"),
    PDA40012033(40012033, "条码参数不能为空"),

    // endregion


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
