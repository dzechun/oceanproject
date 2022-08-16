package com.fantechs.common.base.general.entity.eam;

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
 * 设备条码
 * eam_equipment_barcode
 * @author admin
 * @date 2021-08-20 17:06:54
 */
@Data
@Table(name = "eam_equipment_barcode")
public class EamEquipmentBarcode extends ValidGroup implements Serializable {
    /**
     * 设备条码ID
     */
    @ApiModelProperty(name="equipmentBarcodeId",value = "设备条码ID")
    @Excel(name = "设备条码ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "equipment_barcode_id")
    private Long equipmentBarcodeId;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Excel(name = "设备ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 设备条码
     */
    @ApiModelProperty(name="equipmentBarcode",value = "设备条码")
    @Excel(name = "设备条码", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_barcode")
    private String equipmentBarcode;

    /**
     * 设备序号
     */
    @ApiModelProperty(name="equipmentSeqNum",value = "设备序号")
    @Column(name = "equipment_seq_num")
    private String equipmentSeqNum;

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
    @Excel(name = "当前使用天数", height = 20, width = 30,orderNum="") 
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
    @Excel(name = "当前保养累计使用次数", height = 20, width = 30,orderNum="") 
    @Column(name = "current_maintain_usage_time")
    private Integer currentMaintainUsageTime;

    /**
     * 当前保养累计使用天数
     */
    @ApiModelProperty(name="currentMaintainUsageDays",value = "当前保养累计使用天数")
    @Excel(name = "当前保养累计使用天数", height = 20, width = 30,orderNum="") 
    @Column(name = "current_maintain_usage_days")
    private Integer currentMaintainUsageDays;

    /**
     * 设备状态(1-空转运行 2-待料停机 3-上料停机 4-生产中 5-待生产 6-点检中 7-保养中 8-维修中 9-已报废)
     */
    @ApiModelProperty(name="equipmentStatus",value = "设备状态(1-空转运行 2-待料停机 3-上料停机 4-生产中 5-待生产 6-点检中 7-保养中 8-维修中 9-已报废)")
    @Excel(name = "设备状态(1-空转运行 2-待料停机 3-上料停机 4-生产中 5-待生产 6-点检中 7-保养中 8-维修中 9-已报废)", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_status")
    private Byte equipmentStatus;

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
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Transient
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Transient
    private String equipmentName;

    /**
     * 设备描述
     */
    @ApiModelProperty(name="equipmentDesc",value = "设备描述")
    @Transient
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    @Transient
    private String equipmentModel;

    /**
     * 设备类别
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryName",value = "设备类别")
    private String equipmentCategoryName;

    /**
     * 设备类别ID
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryId",value = "设备类别ID")
    private Long equipmentCategoryId;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}