package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 客户端管理
 * smt_client_manage
 * @author 53203
 * @date 2020-12-01 19:17:37
 */
@Data
@Table(name = "ptl_client_manage")
public class PtlClientManage extends ValidGroup implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(name="client_id",value = "客户端Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = update.class,message = "id不能为空")
    private Long clientId;

    /**
     * 客户端名称
     */
    @ApiModelProperty(name="clientName",value = "客户端名称")
    @Excel(name = "客户端名称", height = 20, width = 30,orderNum="1")
    @Column(name = "client_name")
    @NotBlank(message = "客户端名称不能为空")
    private String clientName;

    /**
     * 客户端备注
     */
    @ApiModelProperty(name="clientRemark",value = "客户端备注")
    @Excel(name = "客户端备注", height = 20, width = 30,orderNum="3")
    @Column(name = "client_remark")
    @NotBlank(message = "客户端备注不能为空")
    private String clientRemark;

    /**
     * 客户端类型：
     */
    @ApiModelProperty(name="clientType",value = "客户端类型：")
    @Excel(name = "客户端类型：", height = 20, width = 30,orderNum="4")
    @Column(name = "client_type")
    private String clientType;

    /**
     * 在线状态：1-在线 0-离线
     */
    @ApiModelProperty(name="onlineStatus",value = "在线状态：1-在线 0-离线")
    @Excel(name = "在线状态：1-在线 0-离线", height = 20, width = 30,orderNum="5")
    @Column(name = "online_status")
    private String onlineStatus;

    /**
     * 登录IP
     */
    @ApiModelProperty(name="loginIp",value = "登录IP")
    @Excel(name = "登录IP", height = 20, width = 30,orderNum="6")
    @Column(name = "login_ip")
    private String loginIp;

    /**
     * 登录密钥
     */
    @ApiModelProperty(name="secretKey",value = "登录密钥")
    @Excel(name = "登录密钥", height = 20, width = 30,orderNum="7")
    @Column(name = "secret_key")
    private String secretKey;

    /**
     * 队列容器名称
     */
    @ApiModelProperty(name="queueName",value = "队列容器名称")
    @Excel(name = "队列容器名称", height = 20, width = 30,orderNum="8")
    @Column(name = "queue_name")
    private String queueName;

    /**
     * 队列容器地址
     */
    @ApiModelProperty(name="queueAddress",value = "队列容器地址")
    @Excel(name = "队列容器地址", height = 20, width = 30,orderNum="9")
    @Column(name = "queue_address")
    private String queueAddress;

    /**
     * 登录时间
     */
    @ApiModelProperty(name="loginTime",value = "登录时间")
    @Excel(name = "登录时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "login_time")
    private Date loginTime;

    /**
     * 离线时间
     */
    @ApiModelProperty(name="offlineTime",value = "离线时间")
    @Excel(name = "离线时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "offline_time")
    private Date offlineTime;

    /**
     * 监听时间
     */
    @ApiModelProperty(name="monitoring_time",value = "监听时间")
    @Excel(name = "监听时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "monitoring_time")
    private Date monitoringTime;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 登录标识（0、系统登录，1、客户端登录）
     */
    @ApiModelProperty(name="loginTag",value = "登录标识（0、系统登录，1、客户端登录）")
    @Transient
    private Byte loginTag;


    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    private static final long serialVersionUID = 1L;
}