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
 * 设备参数列表履历表
 * eam_ht_equipment_param_list
 * @author admin
 * @date 2021-06-25 17:52:35
 */
@Data
@Table(name = "eam_ht_equipment_param_list")
public class EamHtEquipmentParamList extends ValidGroup implements Serializable {
    /**
     * 设备参数列表履历表ID
     */
    @ApiModelProperty(name="htEquipmentParamListId",value = "设备参数列表履历表ID")
    @Excel(name = "设备参数列表履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_param_list_id")
    private Long htEquipmentParamListId;

    /**
     * 设备参数列表ID
     */
    @ApiModelProperty(name="equipmentParamListId",value = "设备参数列表ID")
    @Excel(name = "设备参数列表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_param_list_id")
    private Long equipmentParamListId;

    /**
     * 设备参数设备表ID
     */
    @ApiModelProperty(name="equipmentParamId",value = "设备参数设备表ID")
    @Excel(name = "设备参数设备表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_param_id")
    private Long equipmentParamId;

    /**
     * 参数名称
     */
    @ApiModelProperty(name="parameterName",value = "参数名称")
    @Excel(name = "参数名称", height = 20, width = 30,orderNum="") 
    @Column(name = "parameter_name")
    private String parameterName;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="") 
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 参考值
     */
    @ApiModelProperty(name="referenceValue",value = "参考值")
    @Excel(name = "参考值", height = 20, width = 30,orderNum="") 
    @Column(name = "reference_value")
    private String referenceValue;

    /**
     * 地址位
     */
    @ApiModelProperty(name="addressLoca",value = "地址位")
    @Excel(name = "地址位", height = 20, width = 30,orderNum="")
    @Column(name = "address_loca")
    private Integer addressLoca;

    /**
     * 最小阀值
     */
    @ApiModelProperty(name="minValue",value = "最小阀值")
    @Excel(name = "最小阀值", height = 20, width = 30,orderNum="")
    @Column(name = "min_value")
    private BigDecimal minValue;

    /**
     * 最大阀值
     */
    @ApiModelProperty(name="maxValue",value = "最大阀值")
    @Excel(name = "最大阀值", height = 20, width = 30,orderNum="")
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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
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