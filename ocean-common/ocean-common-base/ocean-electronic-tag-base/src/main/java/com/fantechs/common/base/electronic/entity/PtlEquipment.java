package com.fantechs.common.base.electronic.entity;

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
@Table(name = "ptl_equipment")
public class PtlEquipment extends ValidGroup implements Serializable {
    /**
     * 设备id
     */
    @ApiModelProperty(name="equipmentId",value = "设备id")
    @Id
    @Column(name = "equipment_id")
    @NotNull(groups = update.class,message = "设备id不能为空")
    private Long equipmentId;

    /**
     * 设备标签ID
     */
    @ApiModelProperty(name="equipmentTagId",value = "设备标签ID")
    @Excel(name = "设备标签ID", height = 20, width = 30,orderNum="")
    @NotNull(groups = update.class, message = "设备标签ID不能为空")
    @Column(name = "equipment_tag_id")
    private String equipmentTagId;

    /**
     * 客户端id
     */
    @ApiModelProperty(name="clientId",value = "客户端id")
    @Column(name = "client_id")
    @NotNull(message = "客户端id不能为空")
    private Long clientId;

    /**
     * 设备编号
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编号")
    @Excel(name = "设备编号", height = 20, width = 30,orderNum="1")
    @Column(name = "equipment_code")
    @NotBlank(message = "设备编号不能为空")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="2")
    @Column(name = "equipment_name")
    @NotBlank(message = "设备名称不能为空")
    private String equipmentName;

    /**
     * 设备数据
     */
    @ApiModelProperty(name="equipmentData",value = "设备数据")
    @Excel(name = "设备数据", height = 20, width = 30,orderNum="3")
    @Column(name = "equipment_data")
    private String equipmentData;

    /**
     * 设备IP地址
     */
    @ApiModelProperty(name="equipmentIp",value = "设备IP地址")
    @Excel(name = "设备IP地址", height = 20, width = 30,orderNum="4")
    @Column(name = "equipment_ip")
    private String equipmentIp;

    /**
     * 设备端口
     */
    @ApiModelProperty(name="equipmentPort",value = "设备端口")
    @Excel(name = "设备端口", height = 20, width = 30,orderNum="5")
    @Column(name = "equipment_port")
    private String equipmentPort;

    /**
     * 设备类型(0-控制器 1-区域灯 2-其他)
     */
    @ApiModelProperty(name="equipmentType",value = "设备类型(0-控制器 1-区域灯 2-其他)")
    @Excel(name = "设备类型(0-控制器 1-区域灯 2-其他)", height = 20, width = 30,orderNum="6")
    @Column(name = "equipment_type")
    private Byte equipmentType;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="7")
    @Column(name = "status")
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

    private static final long serialVersionUID = 1L;
}
