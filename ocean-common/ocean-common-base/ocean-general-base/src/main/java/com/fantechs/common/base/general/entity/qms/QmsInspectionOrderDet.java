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
import javax.print.DocFlavor;
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
    @Id
    @Column(name = "inspection_order_det_id")
    private Long inspectionOrderDetId;

    /**
     * 检验单ID
     */
    @ApiModelProperty(name="inspectionOrderId",value = "检验单ID")
    @Column(name = "inspection_order_id")
    private Long inspectionOrderId;

    /**
     * 检验标准明细ID
     */
    @ApiModelProperty(name="inspectionStandardDetId",value = "检验标准明细ID")
    @Column(name = "inspection_standard_det_id")
    private Long inspectionStandardDetId;

    /**
     * 不良数量
     */
    @ApiModelProperty(name="badnessQty",value = "不良数量")
    @Excel(name = "不良数量", height = 20, width = 30,orderNum="11")
    @Column(name = "badness_qty")
    private BigDecimal badnessQty;

    /**
     * 检验结果(0-否 1-是)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-否 1-是)")
    @Excel(name = "检验结果(0-否 1-是)", height = 20, width = 30,orderNum="12")
    @Column(name = "inspection_result")
    private Byte inspectionResult;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 是否必检(0-否 1-是)
     */
    @Column(name = "if_must_inspection")
    @ApiModelProperty(name = "ifMustInspection",value = "是否必检(0-否 1-是)")
    @Excel(name = "是否必检(0-否 1-是)", height = 20, width = 30,orderNum="1")
    private Byte ifMustInspection;

    /**
     * 检验标识(1-定性 2-定量)
     */
    @Column(name = "inspection_tag")
    @ApiModelProperty(name = "inspectionTag",value = "检验标识(1-定性 2-定量)")
    @Excel(name = "检验标识(1-定性 2-定量)", height = 20, width = 30,orderNum="3")
    private Byte inspectionTag;

    /**
     * 规格上限
     */
    @Column(name = "specification_upper_limit")
    @ApiModelProperty(name = "specificationUpperLimit",value = "规格上限")
    @Excel(name = "规格上限", height = 20, width = 30,orderNum="5")
    private BigDecimal specificationUpperLimit;

    /**
     * 规格下限
     */
    @Column(name = "specification_floor")
    @ApiModelProperty(name = "specificationFloor",value = "规格下限")
    @Excel(name = "规格下限", height = 20, width = 30,orderNum="6")
    private BigDecimal specificationFloor;

    /**
     * 单位名称
     */
    @Column(name = "unit_name")
    @ApiModelProperty(name = "unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="7")
    private String unitName;

    /**
     * AQL值
     */
    @Column(name = "aql_value")
    @ApiModelProperty(name = "aqlValue",value = "AQL值")
    @Excel(name = "AQL值", height = 20, width = 30,orderNum="8")
    private BigDecimal aqlValue;

    /**
     * AC值
     */
    @Column(name = "ac_value")
    @ApiModelProperty(name = "acValue",value = "AC值")
    @Excel(name = "AC值", height = 20, width = 30,orderNum="9")
    private Integer acValue;

    /**
     * RE值
     */
    @Column(name = "re_value")
    @ApiModelProperty(name = "reValue",value = "RE值")
    @Excel(name = "RE值", height = 20, width = 30,orderNum="10")
    private Integer reValue;

    /**
     * 样本数
     */
    @Column(name = "sample_qty")
    @ApiModelProperty(name = "sampleQty",value = "样本数")
    @Excel(name = "样本数", height = 20, width = 30,orderNum="4")
    private BigDecimal sampleQty;

    /**
     * 抽样类型(1-固定抽样 2-全检 3-百分比抽样 4-抽样方案)
     */
    @Transient
    @ApiModelProperty(name = "sampleProcessType",value = "抽样类型(1-固定抽样 2-全检 3-百分比抽样 4-抽样方案)")
    private Byte sampleProcessType;

    /**
     * 抽样过程id
     */
    @Transient
    @ApiModelProperty(name = "sampleProcessId",value = "抽样过程id")
    private Long sampleProcessId;

    /**
     * 检验项目-小类
     */
    @ApiModelProperty(name="smallInspectionItemDesc" ,value="检验项目-小类")
    @Column(name = "small_inspection_item_desc")
    @Excel(name = "检验项目-小类", height = 20, width = 30,orderNum="5")
    private String smallInspectionItemDesc;

    /**
     * 检验项目-大类
     */
    @ApiModelProperty(name="bigInspectionItemDesc" ,value="检验项目-大类")
    @Column(name = "big_inspection_item_desc")
    @Excel(name = "检验项目-大类", height = 20, width = 30,orderNum="5")
    private String bigInspectionItemDesc;

    /**
     * 检验项目标准
     */
    @ApiModelProperty(name="inspectionStandardName" ,value="检验项目标准")
    @Column(name = "inspection_standard_name")
    @Excel(name = "检验项目标准", height = 20, width = 30,orderNum="5")
    private String inspectionStandardName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}