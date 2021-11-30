package com.fantechs.common.base.general.entity.esop;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 设备信息
 * esop_equipment
 * @author admin
 * @date 2021-06-25 11:14:58
 */
@Data
@Table(name = "esop_equipment")
public class EsopEquipment extends ValidGroup implements Serializable {
    /**
     * 设备信息ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备信息ID")
    @Id
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
    @ApiModelProperty(name="maintainProjectId",value = "保养项目ID")
    @Column(name = "maintain_project_id")
    private Long maintainProjectId;

    /**
     * 设备IP
     */
    @ApiModelProperty(name="equipmentIp",value = "设备IP")
    @Column(name = "equipment_ip")
    private String equipmentIp;

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
     * 上次保养时间
     */
    @ApiModelProperty(name="lastTimeMaintainTime",value = "上次保养时间")
    @Excel(name = "上次保养时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    @Column(name = "last_time_maintain_time")
    private Date lastTimeMaintainTime;

    /**
     * 下次保养时间
     */
    @ApiModelProperty(name="nextTimeMaintainTime",value = "下次保养时间")
    @Excel(name = "下次保养时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    @Column(name = "next_time_maintain_time")
    private Date nextTimeMaintainTime;

    /**
     * 生命周期(天)
     */
    @ApiModelProperty(name="lifecycle",value = "生命周期(天)")
    @Excel(name = "生命周期(天)", height = 20, width = 30,orderNum="12")
    private BigDecimal lifecycle;

    /**
     * 当前使用次数
     */
    @ApiModelProperty(name="currentUsageTimes",value = "当前使用次数")
    @Excel(name = "当前使用次数", height = 20, width = 30,orderNum="13")
    @Column(name = "current_usage_times")
    private Integer currentUsageTimes;

    /**
     * 最大使用次数
     */
    @ApiModelProperty(name="maxUsageTimes",value = "最大使用次数")
    @Excel(name = "最大使用次数", height = 20, width = 30,orderNum="14")
    @Column(name = "max_usage_times")
    private Integer maxUsageTimes;

    /**
     * 使用状态(1-使用中 2-空闲)
     */
    @ApiModelProperty(name="usageStatus",value = "使用状态(1-使用中 2-空闲)")
    @Excel(name = "使用状态(1-使用中 2-空闲)", height = 20, width = 30,orderNum="15",replace = {"使用中_1", "空闲_2"})
    @Column(name = "usage_status")
    private Byte usageStatus;

    /**
     * 线上状态(0-离线 1-在线)
     */
    @ApiModelProperty(name="onlineStatus",value = "线上状态(0-离线 1-在线 2-已登录 3-中心异常)")
    @Column(name = "online_status")
    private Byte onlineStatus;

    @ApiModelProperty(name="xaxis",value = "X坐标")
    @Column(name = "x_axis")
    private BigDecimal xaxis;

    @ApiModelProperty(name="yaxis",value = "Y坐标")
    @Column(name = "y_axis")
    private BigDecimal yaxis;


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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}