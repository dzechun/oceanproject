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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 设备信息履历表
 * eam_ht_equipment
 * @author admin
 * @date 2021-06-25 11:14:58
 */
@Data
@Table(name = "eam_ht_equipment")
public class EamHtEquipment extends ValidGroup implements Serializable {
    /**
     * 设备信息履历ID
     */
    @ApiModelProperty(name="htEquipmentId",value = "设备信息履历ID")
    @Excel(name = "设备信息履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_id")
    private Long htEquipmentId;

    /**
     * 设备信息ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备信息ID")
    @Excel(name = "设备信息ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_code")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_name")
    private String equipmentName;

    /**
     * 设备描述
     */
    @ApiModelProperty(name="equipmentDesc",value = "设备描述")
    @Excel(name = "设备描述", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_desc")
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_model")
    private String equipmentModel;

    /**
     * 设备类别ID
     */
    @ApiModelProperty(name="equipmentCategoryId",value = "设备类别ID")
    @Excel(name = "设备类别ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_category_id")
    private Long equipmentCategoryId;

    /**
     * 功率(kw)
     */
    @ApiModelProperty(name="power",value = "功率(kw)")
    @Excel(name = "功率(kw)", height = 20, width = 30,orderNum="") 
    private BigDecimal power;

    /**
     * 重量(kg)
     */
    @ApiModelProperty(name="weight",value = "重量(kg)")
    @Excel(name = "重量(kg)", height = 20, width = 30,orderNum="") 
    private BigDecimal weight;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 出厂日期
     */
    @ApiModelProperty(name="releaseDate",value = "出厂日期")
    @Excel(name = "出厂日期", height = 20, width = 30,orderNum="") 
    @Column(name = "release_date")
    private Date releaseDate;

    /**
     * 上次保养时间
     */
    @ApiModelProperty(name="lastTimeMaintainTime",value = "上次保养时间")
    @Excel(name = "上次保养时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_time_maintain_time")
    private Date lastTimeMaintainTime;

    /**
     * 下次保养时间
     */
    @ApiModelProperty(name="nextTimeMaintainTime",value = "下次保养时间")
    @Excel(name = "下次保养时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "next_time_maintain_time")
    private Date nextTimeMaintainTime;

    /**
     * 生命周期(天)
     */
    @ApiModelProperty(name="lifecycle",value = "生命周期(天)")
    @Excel(name = "生命周期(天)", height = 20, width = 30,orderNum="") 
    private BigDecimal lifecycle;

    /**
     * 当前使用次数
     */
    @ApiModelProperty(name="currentUsageTimes",value = "当前使用次数")
    @Excel(name = "当前使用次数", height = 20, width = 30,orderNum="") 
    @Column(name = "current_usage_times")
    private Integer currentUsageTimes;

    /**
     * 最大使用次数
     */
    @ApiModelProperty(name="maxUsageTimes",value = "最大使用次数")
    @Excel(name = "最大使用次数", height = 20, width = 30,orderNum="") 
    @Column(name = "max_usage_times")
    private Integer maxUsageTimes;

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
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum="8")
    private String organizationName;

    /**
     * 设备类别
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryDesc",value = "设备类别")
    @Excel(name = "设备类别", height = 20, width = 30,orderNum="8")
    private String equipmentCategoryDesc;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="8")
    private String warehouseName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}