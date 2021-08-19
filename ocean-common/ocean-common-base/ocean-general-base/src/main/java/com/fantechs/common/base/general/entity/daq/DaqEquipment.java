package com.fantechs.common.base.general.entity.daq;

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
 * daq_equipment
 * @author admin
 * @date 2021-06-25 11:14:58
 */
@Data
@Table(name = "daq_equipment")
public class DaqEquipment extends ValidGroup implements Serializable {
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
     * 线上状态(0-离线 1-在线)
     */
    @ApiModelProperty(name="onlineStatus",value = "线上状态(0-离线 1-在线 2-已登录 3-中心异常)")
    @Column(name = "online_status")
    private Byte onlineStatus;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}