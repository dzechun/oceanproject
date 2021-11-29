package com.fantechs.common.base.general.entity.jinan;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * RFID资产管理基站日志表
 * rfid_base_station_log
 * @author admin
 * @date 2021-11-29 16:39:17
 */
@Data
@Table(name = "rfid_base_station_log")
public class RfidBaseStationLog extends ValidGroup implements Serializable {
    /**
     * 基站日志ID
     */
    @ApiModelProperty(name="baseStationLogId",value = "基站日志ID")
    @Id
    @Column(name = "base_station_log_id")
    private Long baseStationLogId;

    /**
     * 区域ID
     */
    @ApiModelProperty(name="areaId",value = "区域ID")
    @Column(name = "area_id")
    private Long areaId;

    /**
     * 基站ID
     */
    @ApiModelProperty(name="baseStationId",value = "基站ID")
    @Column(name = "base_station_id")
    private Long baseStationId;

    /**
     * 是否正常(0-否 1-是)
     */
    @ApiModelProperty(name="ifNormal",value = "是否正常(0-否 1-是)")
    @Excel(name = "是否正常(0-否 1-是)", height = 20, width = 30,orderNum="3")
    @Column(name = "if_normal")
    private Byte ifNormal;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 正常信息
     */
    @ApiModelProperty(name="normalInfo",value = "正常信息")
    @Excel(name = "正常信息", height = 20, width = 30,orderNum="4")
    @Column(name = "normal_info")
    private String normalInfo;

    /**
     * 不正常信息
     */
    @ApiModelProperty(name="abnormalInfo",value = "不正常信息")
    @Excel(name = "不正常信息", height = 20, width = 30,orderNum="5")
    @Column(name = "abnormal_info")
    private String abnormalInfo;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="6")
    private String remark;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 区域编码
     */
    @Transient
    @ApiModelProperty(name = "areaCode",value = "区域编码")
    private String areaCode;

    /**
     * 区域名称
     */
    @Transient
    @ApiModelProperty(name = "areaName",value = "区域名称")
    @Excel(name = "区域名称", height = 20, width = 30,orderNum="1")
    private String areaName;

    /**
     * 基站编码
     */
    @Transient
    @ApiModelProperty(name = "baseStationCode",value = "基站编码")
    private String baseStationCode;

    /**
     * 基站名称
     */
    @Transient
    @ApiModelProperty(name = "baseStationName",value = "基站名称")
    @Excel(name = "基站名称", height = 20, width = 30,orderNum="2")
    private String baseStationName;

    private static final long serialVersionUID = 1L;
}