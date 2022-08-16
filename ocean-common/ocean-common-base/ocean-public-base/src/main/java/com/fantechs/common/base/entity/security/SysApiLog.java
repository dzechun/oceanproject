package com.fantechs.common.base.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 接口日志
 * sys_api_log
 * @author bgkun
 * @date 2021-07-09 14:21:50
 */
@Data
@Table(name = "sys_api_log")
public class SysApiLog extends ValidGroup implements Serializable {
    /**
     * 日志ID
     */
    @ApiModelProperty(name="apiLogId",value = "日志ID")
    @Excel(name = "日志ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "api_log_id")
    private Long apiLogId;

    /**
     * 第三方系统名称
     */
    @ApiModelProperty(name="thirdpartySysName",value = "第三方系统名称")
    @Excel(name = "第三方系统名称", height = 20, width = 30,orderNum="") 
    @Column(name = "thirdparty_sys_name")
    private String thirdpartySysName;

    /**
     * 调用结果(0-失败 1-成功)
     */
    @ApiModelProperty(name="callResult",value = "调用结果(0-失败 1-成功)")
    @Excel(name = "调用结果(0-失败 1-成功)", height = 20, width = 30,orderNum="") 
    @Column(name = "call_result")
    private Byte callResult;

    /**
     * 调用类型(1-主动 2-被动)
     */
    @ApiModelProperty(name="callType",value = "调用类型(1-主动 2-被动)")
    @Excel(name = "调用类型(1-主动 2-被动)", height = 20, width = 30,orderNum="") 
    @Column(name = "call_type")
    private Byte callType;

    /**
     * 接口模块
     */
    @ApiModelProperty(name="apiModule",value = "接口模块")
    @Excel(name = "接口模块", height = 20, width = 30,orderNum="") 
    @Column(name = "api_module")
    private String apiModule;

    /**
     * 第三方接口IP端口
     */
    @ApiModelProperty(name="thirdpartyApiIpPort",value = "第三方接口IP端口")
    @Excel(name = "第三方接口IP端口", height = 20, width = 30,orderNum="") 
    @Column(name = "thirdparty_api_ip_port")
    private String thirdpartyApiIpPort;

    /**
     * 接口URL
     */
    @ApiModelProperty(name="apiUrl",value = "接口URL")
    @Excel(name = "接口URL", height = 20, width = 30,orderNum="") 
    @Column(name = "api_url")
    private String apiUrl;

    /**
     * 耗时
     */
    @ApiModelProperty(name="consumeTime",value = "耗时")
    @Excel(name = "耗时", height = 20, width = 30,orderNum="") 
    @Column(name = "consume_time")
    private BigDecimal consumeTime;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 调用时间
     */
    @ApiModelProperty(name="requestTime",value = "调用时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "request_time")
    private Date requestTime;

    /**
     * 响应时间
     */
    @ApiModelProperty(name="responseTime",value = "响应时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "response_time")
    private Date responseTime;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 返回数据
     */
    @ApiModelProperty(name="responseData",value = "返回数据")
    @Excel(name = "返回数据", height = 20, width = 30,orderNum="") 
    @Column(name = "response_data")
    private String responseData;

    /**
     * 请求参数
     */
    @ApiModelProperty(name="requestParameter",value = "请求参数")
    @Excel(name = "请求参数", height = 20, width = 30,orderNum="") 
    @Column(name = "request_parameter")
    private String requestParameter;

    private static final long serialVersionUID = 1L;
}