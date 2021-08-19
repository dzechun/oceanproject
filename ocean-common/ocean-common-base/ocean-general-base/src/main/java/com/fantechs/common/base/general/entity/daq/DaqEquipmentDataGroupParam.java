package com.fantechs.common.base.general.entity.daq;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 设备数据组参数表
 * daq_equipment_data_group_param
 * @author 81947
 * @date 2021-08-02 09:15:39
 */
@Data
@Table(name = "daq_equipment_data_group_param")
public class DaqEquipmentDataGroupParam extends ValidGroup implements Serializable {
    /**
     * 设备数据组参数ID
     */
    @ApiModelProperty(name="equipmentDataGroupParamId",value = "设备数据组参数ID")
    @Excel(name = "设备数据组参数ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "equipment_data_group_param_id")
    private Long equipmentDataGroupParamId;

    /**
     * 设备组ID
     */
    @ApiModelProperty(name="equipmentDataGroupId",value = "设备组ID")
    @Excel(name = "设备组ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_data_group_id")
    private Long equipmentDataGroupId;

    /**
     * 参数名称
     */
    @ApiModelProperty(name="paramName",value = "参数名称")
    @Excel(name = "参数名称", height = 20, width = 30,orderNum="") 
    @Column(name = "param_name")
    private String paramName;

    /**
     * 字段名
     */
    @ApiModelProperty(name="fieldName",value = "字段名")
    @Excel(name = "字段名", height = 20, width = 30,orderNum="") 
    @Column(name = "field_name")
    private String fieldName;

    /**
     * 地址位
     */
    @ApiModelProperty(name="addressLoca",value = "地址位")
    @Excel(name = "地址位", height = 20, width = 30,orderNum="") 
    @Column(name = "address_loca")
    private Integer addressLoca;

    /**
     * 最小阈值
     */
    @ApiModelProperty(name="minValue",value = "最小阈值")
    @Excel(name = "最小阈值", height = 20, width = 30,orderNum="") 
    @Column(name = "min_value")
    private BigDecimal minValue;

    /**
     * 最大阈值
     */
    @ApiModelProperty(name="maxValue",value = "最大阈值")
    @Excel(name = "最大阈值", height = 20, width = 30,orderNum="") 
    @Column(name = "max_value")
    private BigDecimal maxValue;

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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}