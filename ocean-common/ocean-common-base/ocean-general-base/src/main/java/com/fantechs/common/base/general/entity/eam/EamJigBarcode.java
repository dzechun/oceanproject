package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 治具信息条码
 * eam_jig_barcode
 * @author admin
 * @date 2021-07-28 11:44:14
 */
@Data
@Table(name = "eam_jig_barcode")
public class EamJigBarcode extends ValidGroup implements Serializable {
    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Id
    @Column(name = "jig_barcode_id")
    @NotNull(groups = update.class,message = "治具条码ID不能为空")
    private Long jigBarcodeId;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 治具条码
     */
    @ApiModelProperty(name="jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="1")
    @Column(name = "jig_barcode")
    private String jigBarcode;

    /**
     * 资产编码
     */
    @ApiModelProperty(name="assetCode",value = "资产编码")
    @Excel(name = "资产编码", height = 20, width = 30,orderNum="2")
    @Column(name = "asset_code")
    private String assetCode;

    /**
     * 当前使用次数 currentUsageTime
     */
    @ApiModelProperty(name="currentUsageTime",value = "当前使用次数")
    @Excel(name = "当前使用次数", height = 20, width = 30,orderNum="3")
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
    @Excel(name = "上次保养时间", height = 20, width = 30,orderNum="4",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_time_maintain_time")
    private Date lastTimeMaintainTime;

    /**
     * 当前保养次数
     */
    @ApiModelProperty(name="currentMaintainTime",value = "当前保养次数")
    @Excel(name = "当前保养次数", height = 20, width = 30,orderNum="5")
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
     * 使用状态(1-空闲 2-使用中 3-点检中 4-保养中 5-维修中 6-已报废)
     */
    @ApiModelProperty(name="usageStatus",value = "使用状态(1-空闲 2-使用中 3-点检中 4-保养中 5-维修中 6-已报废)")
    @Excel(name = "使用状态(1-空闲 2-使用中 3-点检中 4-保养中 5-维修中 6-已报废)", height = 20, width = 30,orderNum="6")
    @Column(name = "usage_status")
    private Byte usageStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}