package com.fantechs.common.base.general.entity.qms;

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
 * 检验单明细
 * qms_inspection_order_det
 * @author admin
 * @date 2021-05-25 10:24:14
 */
@Data
@Table(name = "qms_inspection_order_det")
public class QmsInspectionOrderDet extends ValidGroup implements Serializable {
    /**
     * 检验单明细ID
     */
    @ApiModelProperty(name="inspectionOrderDetId",value = "检验单明细ID")
    @Excel(name = "检验单明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "inspection_order_det_id")
    private Long inspectionOrderDetId;

    /**
     * 检验单ID
     */
    @ApiModelProperty(name="inspectionOrderId",value = "检验单ID")
    @Excel(name = "检验单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_order_id")
    private Long inspectionOrderId;

    /**
     * 检验标准明细ID
     */
    @ApiModelProperty(name="inspectionStandardDetId",value = "检验标准明细ID")
    @Excel(name = "检验标准明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_standard_det_id")
    private Long inspectionStandardDetId;

    /**
     * 不良数量
     */
    @ApiModelProperty(name="badnessQty",value = "不良数量")
    @Excel(name = "不良数量", height = 20, width = 30,orderNum="") 
    @Column(name = "badness_qty")
    private BigDecimal badnessQty;

    /**
     * 检验结果(0-否 1-是)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-否 1-是)")
    @Excel(name = "检验结果(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_result")
    private Byte inspectionResult;

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
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 检验项目
     */
    @Transient
    @ApiModelProperty(name = "inspectionItem",value = "检验项目")
    private String inspectionItem;

    /**
     * 检验标识
     */
    @Transient
    @ApiModelProperty(name = "inspectionTag",value = "检验标识")
    private Byte inspectionTag;

    /**
     * 规格上限
     */
    @Transient
    @ApiModelProperty(name = "specificationUpperLimit",value = "规格上限")
    private BigDecimal specificationUpperLimit;

    /**
     * 规格下限
     */
    @Transient
    @ApiModelProperty(name = "specificationFloor",value = "规格下限")
    private BigDecimal specificationFloor;

    /**
     * 单位名称
     */
    @Transient
    @ApiModelProperty(name = "unitName",value = "单位名称")
    private String unitName;

    /**
     * AQL值
     */
    @Transient
    @ApiModelProperty(name = "aqlValue",value = "AQL值")
    private BigDecimal aqlValue;

    /**
     * AC值
     */
    @Transient
    @ApiModelProperty(name = "acValue",value = "AC值")
    private Integer acValue;

    /**
     * RE值
     */
    @Transient
    @ApiModelProperty(name = "reValue",value = "RE值")
    private Integer reValue;

    /**
     * 样本数
     */
    @Transient
    @ApiModelProperty(name = "sampleQty",value = "样本数")
    private BigDecimal sampleQty;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}