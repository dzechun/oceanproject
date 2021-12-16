package com.fantechs.common.base.general.entity.mulinsen;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * hr_user_info
 * @author 86178
 * @date 2021-12-06 20:58:17
 */
@Data
@Table(name = "hr_user_info")
public class HrUserInfo extends ValidGroup implements Serializable {
    /**
     * 所属部门ID
     */
    @ApiModelProperty(name="deptId",value = "所属部门ID")
    @Excel(name = "所属部门ID", height = 20, width = 30,orderNum="") 
    @Column(name = "DEPT_ID")
    private String deptId;

    /**
     * 域账户
     */
    @ApiModelProperty(name="domainAccount",value = "域账户")
    @Excel(name = "域账户", height = 20, width = 30,orderNum="") 
    @Column(name = "DOMAIN_ACCOUNT")
    private String domainAccount;

    /**
     * 邮箱
     */
    @ApiModelProperty(name="email",value = "邮箱")
    @Excel(name = "邮箱", height = 20, width = 30,orderNum="") 
    @Column(name = "EMAIL")
    private String email;

    /**
     * Hr人员ID
     */
    @ApiModelProperty(name="empId",value = "Hr人员ID")
    @Excel(name = "Hr人员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "EMP_ID")
    private String empId;

    /**
     * 入职日期
     */
    @ApiModelProperty(name="entrydate",value = "入职日期")
    @Excel(name = "入职日期", height = 20, width = 30,orderNum="") 
    @Column(name = "ENTRYDATE")
    private Date entrydate;

    /**
     * 性别:1男，2女
     */
    @ApiModelProperty(name="gender",value = "性别:1男，2女")
    @Excel(name = "性别:1男，2女", height = 20, width = 30,orderNum="") 
    @Column(name = "GENDER")
    private String gender;

    /**
     * 身份证
     */
    @ApiModelProperty(name="idCard",value = "身份证")
    @Excel(name = "身份证", height = 20, width = 30,orderNum="") 
    @Column(name = "ID_CARD")
    private String idCard;

    /**
     * 是否删除：0未删除、1已删除
     */
    @ApiModelProperty(name="isdel",value = "是否删除：0未删除、1已删除")
    @Excel(name = "是否删除：0未删除、1已删除", height = 20, width = 30,orderNum="") 
    @Column(name = "ISDEL")
    private Integer isdel;

    /**
     * 职级
     */
    @ApiModelProperty(name="jobGrade",value = "职级")
    @Excel(name = "职级", height = 20, width = 30,orderNum="") 
    @Column(name = "JOB_GRADE")
    private String jobGrade;

    /**
     * 职等
     */
    @ApiModelProperty(name="jobLevel",value = "职等")
    @Excel(name = "职等", height = 20, width = 30,orderNum="") 
    @Column(name = "JOB_LEVEL")
    private String jobLevel;

    /**
     * 工号
     */
    @ApiModelProperty(name="jobNum",value = "工号")
    @Excel(name = "工号", height = 20, width = 30,orderNum="") 
    @Column(name = "JOB_NUM")
    private String jobNum;

    /**
     * 职位名称
     */
    @ApiModelProperty(name="jobTitle",value = "职位名称")
    @Excel(name = "职位名称", height = 20, width = 30,orderNum="") 
    @Column(name = "JOB_TITLE")
    private String jobTitle;

    /**
     * 离职日期
     */
    @ApiModelProperty(name="leavedate",value = "离职日期")
    @Excel(name = "离职日期", height = 20, width = 30,orderNum="") 
    @Column(name = "LEAVEDATE")
    private Date leavedate;

    /**
     * 主管ID
     */
    @ApiModelProperty(name="managerId",value = "主管ID")
    @Excel(name = "主管ID", height = 20, width = 30,orderNum="") 
    @Column(name = "MANAGER_ID")
    private String managerId;

    /**
     * 手机号
     */
    @ApiModelProperty(name="mobilePhone",value = "手机号")
    @Excel(name = "手机号", height = 20, width = 30,orderNum="") 
    @Column(name = "MOBILE_PHONE")
    private String mobilePhone;

    /**
     * 姓名
     */
    @ApiModelProperty(name="realName",value = "姓名")
    @Excel(name = "姓名", height = 20, width = 30,orderNum="") 
    @Column(name = "REAL_NAME")
    private String realName;

    /**
     * 数据状态：0未同步，1已同步, 2已变更
     */
    @ApiModelProperty(name="dataStatus",value = "数据状态：0未同步，1已同步, 2已变更")
    @Excel(name = "数据状态：0未同步，1已同步, 2已变更", height = 20, width = 30,orderNum="") 
    @Column(name = "DATA_STATUS")
    private Integer dataStatus;

    /**
     * 同步时间
     */
    @ApiModelProperty(name="syncTime",value = "同步时间")
    @Excel(name = "同步时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "SYNC_TIME")
    private Date syncTime;

    private static final long serialVersionUID = 1L;
}