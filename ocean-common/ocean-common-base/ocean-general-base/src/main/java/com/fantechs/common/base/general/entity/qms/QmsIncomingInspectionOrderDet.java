package com.fantechs.common.base.general.entity.qms;

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
 * 来料检验单明细
 * qms_incoming_inspection_order_det
 * @author admin
 * @date 2021-12-06 10:30:34
 */
@Data
@Table(name = "qms_incoming_inspection_order_det")
public class QmsIncomingInspectionOrderDet extends ValidGroup implements Serializable {
    /**
     * 来料检验单明细ID
     */
    @ApiModelProperty(name="incomingInspectionOrderDetId",value = "来料检验单明细ID")
    @Id
    @Column(name = "incoming_inspection_order_det_id")
    private Long incomingInspectionOrderDetId;

    /**
     * 来料检验单ID
     */
    @ApiModelProperty(name="incomingInspectionOrderId",value = "来料检验单ID")
    @Column(name = "incoming_inspection_order_id")
    private Long incomingInspectionOrderId;

    /**
     * 是否必检(0-否 1-是)
     */
    @ApiModelProperty(name="ifMustInspection",value = "是否必检(0-否 1-是)")
    @Excel(name = "是否必检(0-否 1-是)", height = 20, width = 30,orderNum="1")
    @Column(name = "if_must_inspection")
    private Byte ifMustInspection;

    /**
     * 检验项目-大类
     */
    @ApiModelProperty(name="bigInspectionItemDesc",value = "检验项目-大类")
    @Excel(name = "检验项目-大类", height = 20, width = 30,orderNum="2")
    @Column(name = "big_inspection_item_desc")
    private String bigInspectionItemDesc;

    /**
     * 检验项目-小类
     */
    @ApiModelProperty(name="smallInspectionItemDesc",value = "检验项目-小类")
    @Excel(name = "检验项目-小类", height = 20, width = 30,orderNum="3")
    @Column(name = "small_inspection_item_desc")
    private String smallInspectionItemDesc;

    /**
     * 检验项目标准名称
     */
    @ApiModelProperty(name="inspectionStandardName",value = "检验项目标准名称")
    @Excel(name = "检验项目标准名称", height = 20, width = 30,orderNum="4")
    @Column(name = "inspection_standard_name")
    private String inspectionStandardName;

    /**
     * 检验标识(1-定性 2-定量)
     */
    @ApiModelProperty(name="inspectionTag",value = "检验标识(1-定性 2-定量)")
    @Excel(name = "检验标识(1-定性 2-定量)", height = 20, width = 30,orderNum="5")
    @Column(name = "inspection_tag")
    private Byte inspectionTag;

    /**
     * 样本数
     */
    @ApiModelProperty(name="sampleQty",value = "样本数")
    @Excel(name = "样本数", height = 20, width = 30,orderNum="6")
    @Column(name = "sample_qty")
    private BigDecimal sampleQty;

    /**
     * 规格上限
     */
    @ApiModelProperty(name="specificationUpperLimit",value = "规格上限")
    @Excel(name = "规格上限", height = 20, width = 30,orderNum="7")
    @Column(name = "specification_upper_limit")
    private BigDecimal specificationUpperLimit;

    /**
     * 规格下限
     */
    @ApiModelProperty(name="specificationFloor",value = "规格下限")
    @Excel(name = "规格下限", height = 20, width = 30,orderNum="8")
    @Column(name = "specification_floor")
    private BigDecimal specificationFloor;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="9")
    @Column(name = "unit_name")
    private String unitName;

    /**
     * AQL值
     */
    @ApiModelProperty(name="aqlValue",value = "AQL值")
    @Excel(name = "AQL值", height = 20, width = 30,orderNum="10")
    @Column(name = "aql_value")
    private BigDecimal aqlValue;

    /**
     * AC值
     */
    @ApiModelProperty(name="acValue",value = "AC值")
    @Excel(name = "AC值", height = 20, width = 30,orderNum="11")
    @Column(name = "ac_value")
    private Integer acValue;

    /**
     * RE值
     */
    @ApiModelProperty(name="reValue",value = "RE值")
    @Excel(name = "RE值", height = 20, width = 30,orderNum="12")
    @Column(name = "re_value")
    private Integer reValue;

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
    @Excel(name = "不良数量", height = 20, width = 30,orderNum="13")
    @Column(name = "badness_qty")
    private BigDecimal badnessQty;

    /**
     * 不良类别ID
     */
    @ApiModelProperty(name="badnessCategoryId",value = "不良类别ID")
    @Column(name = "badness_category_id")
    private Long badnessCategoryId;

    /**
     * 检验结果(0-否 1-是)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-否 1-是)")
    @Excel(name = "检验结果(0-否 1-是)", height = 20, width = 30,orderNum="14")
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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}