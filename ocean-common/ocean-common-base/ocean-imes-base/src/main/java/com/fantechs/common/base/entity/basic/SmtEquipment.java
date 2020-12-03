package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 设备管理表
 * smt_equipment
 * @author 53203
 * @date 2020-11-23 16:08:34
 */
@Data
@Table(name = "smt_equipment")
public class SmtEquipment extends ValidGroup implements Serializable {
    /**
     * 设备id
     */
    @ApiModelProperty(name="equipmentId",value = "设备id")
    @Id
    @Column(name = "equipment_id")
    @NotNull(groups = update.class,message = "设备id不能为空")
    private Long equipmentId;

    /**
     * 客户端编号
     */
    @ApiModelProperty(name="equipmentCode",value = "客户端编号")
    @Excel(name = "客户端编号", height = 20, width = 30,orderNum="1")
    @Column(name = "equipment_code")
    @NotBlank(message = "客户端编号不能为空")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="deviceName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="2")
    @Column(name = "device_name")
    @NotBlank(message = "设备名称不能为空")
    private String deviceName;

    /**
     * 设备数据
     */
    @ApiModelProperty(name="deviceData",value = "设备数据")
    @Excel(name = "设备数据", height = 20, width = 30,orderNum="3")
    @Column(name = "device_data")
    private String deviceData;

    /**
     * 设备类型
     */
    @ApiModelProperty(name="type",value = "设备类型")
    @Excel(name = "设备类型", height = 20, width = 30,orderNum="4")
    private Byte type;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="5")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private static final long serialVersionUID = 1L;
}
