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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @Column(name = "equipment_id")
    @NotNull(groups = update.class,message = "设备信息ID不能为空")
    private Long equipmentId;

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="1")
    @Column(name = "equipment_code")
    @NotBlank(message = "设备编码不能为空")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="2")
    @Column(name = "equipment_name")
    private String equipmentName;

    /**
     * 设备描述
     */
    @ApiModelProperty(name="equipmentDesc",value = "设备描述")
    @Excel(name = "设备描述", height = 20, width = 30,orderNum="3")
    @Column(name = "equipment_desc")
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="4")
    @Column(name = "equipment_model")
    private String equipmentModel;

    /**
     * 设备类别ID
     */
    @ApiModelProperty(name="equipmentCategoryId",value = "设备类别ID")
    @Column(name = "equipment_category_id")
    private Long equipmentCategoryId;

    /**
     * 设备序号
     */
    @ApiModelProperty(name="equipmentSeqNum",value = "设备序号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="4")
    @Column(name = "equipment_seq_num")
    private String equipmentSeqNum;

    /**
     * 功率(kw)
     */
    @ApiModelProperty(name="power",value = "功率(kw)")
    @Excel(name = "功率(kw)", height = 20, width = 30,orderNum="6")
    private BigDecimal power;

    /**
     * 重量(kg)
     */
    @ApiModelProperty(name="weight",value = "重量(kg)")
    @Excel(name = "重量(kg)", height = 20, width = 30,orderNum="7")
    private BigDecimal weight;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Column(name = "pro_line_id")
    private Long proLineId;


    /**
     * 工位ID
     */
    @ApiModelProperty(name="stationId",value = "工位ID")
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId",value = "工段ID")
    @Column(name = "section_id")
    private Long sectionId;

    /**
     * 工厂ID
     */
    @ApiModelProperty(name="factoryId",value = "工厂ID")
    @Column(name = "factory_id")
    private Long factoryId;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId",value = "车间ID")
    @Column(name = "work_shop_id")
    private Long workShopId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 长（cm）
     */
    @ApiModelProperty(name="length",value = "长（cm）")
    @Column(name = "length")
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @ApiModelProperty(name="width",value = "宽(cm)")
    @Column(name = "width")
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @ApiModelProperty(name="height",value = "高(cm)")
    @Column(name = "height")
    private BigDecimal height;

    /**
     * 体积(cm3)
     */
    @ApiModelProperty(name="volume",value = "体积(cm3)")
    @Column(name = "volume")
    private BigDecimal volume;

    /**
     * 保养项目ID
     */
    @ApiModelProperty(name="equipmentMaintainProjectId",value = "保养项目ID")
    @Column(name = "equipment_maintain_project_id")
    private Long equipmentMaintainProjectId;

    /**
     * 设备IP
     */
    @ApiModelProperty(name="equipmentIp",value = "设备IP")
    @Column(name = "equipment_ip")
    private String equipmentIp;

    /**
     * 设备管理员用户ID
     */
    @ApiModelProperty(name="equipmentMgtUserId",value = "设备管理员用户ID")
    @Column(name = "equipment_mgt_user_id")
    private Long equipmentMgtUserId;

    /**
     * MAC地址
     */
    @ApiModelProperty(name="equipmentMacAddress",value = "MAC地址")
    @Column(name = "equipment_mac_address")
    private String equipmentMacAddress;

    /**
     * 出厂日期
     */
    @ApiModelProperty(name="releaseDate",value = "出厂日期")
    @Excel(name = "出厂日期", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd")
    @Column(name = "release_date")
    private Date releaseDate;

    /**
     * 保养周期(天)
     */
    @ApiModelProperty(name="maintainCycle",value = "保养周期(天)")
    @Excel(name = "保养周期(天)", height = 20, width = 30,orderNum="9")
    @Column(name = "maintain_cycle")
    private Integer maintainCycle;

    /**
     * 保养警告天数
     */
    @ApiModelProperty(name="maintainWarningDays",value = "保养警告天数")
    @Excel(name = "保养警告天数", height = 20, width = 30,orderNum="9")
    @Column(name = "maintain_warning_days")
    private Integer maintainWarningDays;

    /**
     * 保养最大使用次数
     */
    @ApiModelProperty(name="maintainMaxUsageTime",value = "保养最大使用次数")
    @Excel(name = "保养最大使用次数", height = 20, width = 30,orderNum="9")
    @Column(name = "maintain_max_usage_time")
    private Integer maintainMaxUsageTime;

    /**
     * 保养警告次数
     */
    @ApiModelProperty(name="maintainWarningTime",value = "保养警告次数")
    @Excel(name = "保养警告次数", height = 20, width = 30,orderNum="9")
    @Column(name = "maintain_warning_time")
    private Integer maintainWarningTime;

    /**
     * 生命周期(天)
     */
    @ApiModelProperty(name="lifecycle",value = "生命周期(天)")
    @Excel(name = "生命周期(天)", height = 20, width = 30,orderNum="12")
    private BigDecimal lifecycle;

    /**
     * 最大使用次数
     */
    @ApiModelProperty(name="maxUsageTime",value = "最大使用次数")
    @Excel(name = "最大使用次数", height = 20, width = 30,orderNum="14")
    @Column(name = "max_usage_time")
    private Integer maxUsageTime;

    /**
     * 警告次数
     */
    @ApiModelProperty(name="warningTime",value = "警告次数")
    @Excel(name = "警告次数", height = 20, width = 30,orderNum="14")
    @Column(name = "warning_time")
    private Integer warningTime;

    /**
     * 最大使用天数
     */
    @ApiModelProperty(name="maxUsageDays",value = "最大使用天数")
    @Excel(name = "最大使用天数", height = 20, width = 30,orderNum="14")
    @Column(name = "max_usage_days")
    private Integer maxUsageDays;

    /**
     * 警告天数
     */
    @ApiModelProperty(name="warningDays",value = "警告天数")
    @Excel(name = "警告天数", height = 20, width = 30,orderNum="14")
    @Column(name = "warning_days")
    private Integer warningDays;

    @ApiModelProperty(name="xAxis",value = "X坐标")
    @Column(name = "x_axis")
    private BigDecimal xAxis;

    @ApiModelProperty(name="yAxis",value = "Y坐标")
    @Column(name = "y_axis")
    private BigDecimal yAxis;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="17",exportFormat ="yyyy-MM-dd HH:mm")
    @JSONField(format ="yyyy-MM-dd HH:mm")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="19",exportFormat ="yyyy-MM-dd HH:mm")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="16")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="18")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 设备类别
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryDesc",value = "设备类别")
    @Excel(name = "设备类别", height = 20, width = 30,orderNum="5")
    private String equipmentCategoryDesc;

    /**
     * 设备管理员用户名称
     */
    @Transient
    @ApiModelProperty(name = "equipmentMgtUserName",value = "设备管理员用户名称")
    @Excel(name = "设备管理员用户名称", height = 20, width = 30,orderNum="18")
    private String equipmentMgtUserName;

    /**
     * 车间名称
     */
    @Transient
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;

    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(name = "proLineName",value = "产线名称")
    private String proLineName;

    /**
     * 工厂名称
     */
    @Transient
    @ApiModelProperty(name = "factoryName",value = "工厂名称")
    private String factoryName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}