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
 * @date 2021-11-30 13:37:17
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
     * 区域名称
     */
    @ApiModelProperty(name="areaName",value = "区域名称")
    @Excel(name = "区域名称", height = 20, width = 30,orderNum="1")
    @Column(name = "area_name")
    private String areaName;

    /**
     * 基站名称
     */
    @ApiModelProperty(name="baseStationName",value = "基站名称")
    @Excel(name = "基站名称", height = 20, width = 30,orderNum="2")
    @Column(name = "base_station_name")
    private String baseStationName;

    /**
     * 读取时间
     */
    @ApiModelProperty(name="readTime",value = "读取时间")
    @Excel(name = "读取时间", height = 20, width = 30,orderNum="3",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "read_time")
    private Date readTime;

    /**
     * 读取结果(0-失败 1-成功)
     */
    @ApiModelProperty(name="readResult",value = "读取结果(0-失败 1-成功)")
    @Excel(name = "读取结果(0-失败 1-成功)", height = 20, width = 30,orderNum="4")
    @Column(name = "read_result")
    private Byte readResult;

    /**
     * RFID条码
     */
    @ApiModelProperty(name="assetBarcode",value = "RFID条码")
    @Excel(name = "RFID条码", height = 20, width = 30,orderNum="5")
    @Column(name = "asset_barcode")
    private String assetBarcode;

    /**
     * 资产名称
     */
    @ApiModelProperty(name="assetName",value = "资产名称")
    @Excel(name = "资产名称", height = 20, width = 30,orderNum="6")
    @Column(name = "asset_name")
    private String assetName;

    /**
     * 反馈内容
     */
    @ApiModelProperty(name="feedbackContent",value = "反馈内容")
    @Excel(name = "反馈内容", height = 20, width = 30,orderNum="7")
    @Column(name = "feedback_content")
    private String feedbackContent;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}