package com.fantechs.common.base.general.entity.eam.history;

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
 * 治具信息条码履历表
 * eam_ht_jig_barcode
 * @author admin
 * @date 2021-07-28 11:44:15
 */
@Data
@Table(name = "eam_ht_jig_barcode")
public class EamHtJigBarcode extends ValidGroup implements Serializable {
    /**
     * 治具条码履历ID
     */
    @ApiModelProperty(name="htJigBarcodeId",value = "治具条码履历ID")
    @Excel(name = "治具条码履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_barcode_id")
    private Long htJigBarcodeId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Excel(name = "治具条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_barcode_id")
    private Long jigBarcodeId;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Excel(name = "治具ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 治具条码
     */
    @ApiModelProperty(name="jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_barcode")
    private String jigBarcode;

    /**
     * 资产编码
     */
    @ApiModelProperty(name="assetCode",value = "资产编码")
    @Excel(name = "资产编码", height = 20, width = 30,orderNum="") 
    @Column(name = "asset_code")
    private String assetCode;

    /**
     * 当前使用次数
     */
    @ApiModelProperty(name="currentUsageTime",value = "当前使用次数")
    @Excel(name = "当前使用次数", height = 20, width = 30,orderNum="") 
    @Column(name = "current_usage_time")
    private Integer currentUsageTime;

    /**
     * 当前使用天数
     */
    @ApiModelProperty(name="currentUsageDays",value = "当前使用天数")
    @Excel(name = "当前使用天数", height = 20, width = 30,orderNum="3")
    @Column(name = "current_usage_days")
    private Integer currentUsageDays;

    /**
     * 上次保养时间
     */
    @ApiModelProperty(name="lastTimeMaintainTime",value = "上次保养时间")
    @Excel(name = "上次保养时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_time_maintain_time")
    private Date lastTimeMaintainTime;

    /**
     * 当前保养次数
     */
    @ApiModelProperty(name="currentMaintainTime",value = "当前保养次数")
    @Excel(name = "当前保养次数", height = 20, width = 30,orderNum="") 
    @Column(name = "current_maintain_time")
    private Integer currentMaintainTime;

    /**
     * 当前保养累计使用次数
     */
    @ApiModelProperty(name="currentMaintainUsageTime",value = "当前保养累计使用次数")
    @Excel(name = "当前保养累计使用次数", height = 20, width = 30,orderNum="5")
    @Column(name = "current_maintain_usage_time")
    private Integer currentMaintainUsageTime;

    /**
     * 当前保养累计使用天数
     */
    @ApiModelProperty(name="currentMaintainUsageDays",value = "当前保养累计使用天数")
    @Excel(name = "当前保养累计使用天数", height = 20, width = 30,orderNum="5")
    @Column(name = "current_maintain_usage_days")
    private Integer currentMaintainUsageDays;

    /**
     * 使用状态(1-使用中 2-空闲)
     */
    @ApiModelProperty(name="usageStatus",value = "使用状态(1-使用中 2-空闲)")
    @Excel(name = "使用状态(1-使用中 2-空闲)", height = 20, width = 30,orderNum="") 
    @Column(name = "usage_status")
    private Byte usageStatus;

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

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}