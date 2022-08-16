package com.fantechs.common.base.general.entity.basic;

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
 * 检验标准明细
 * base_inspection_standard_det
 * @author admin
 * @date 2021-05-19 11:44:04
 */
@Data
@Table(name = "base_inspection_standard_det")
public class BaseInspectionStandardDet extends ValidGroup implements Serializable {
    /**
     * 检验标准明细ID
     */
    @ApiModelProperty(name="inspectionStandardDetId",value = "检验标准明细ID")
    @Id
    @Column(name = "inspection_standard_det_id")
    private Long inspectionStandardDetId;

    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId",value = "检验标准ID")
    @Column(name = "inspection_standard_id")
    private Long inspectionStandardId;

    /**
     * 是否必检(0-否 1-是)
     */
    @ApiModelProperty(name="ifMustInspection",value = "是否必检(0-否 1-是)")
    @Excel(name = "是否必检(0-否 1-是)", height = 20, width = 30,orderNum="1",replace = {"否_0", "是_1"})
    @Column(name = "if_must_inspection")
    private Byte ifMustInspection;

    /**
     * 检验项目ID
     */
    @ApiModelProperty(name="inspectionItemId",value = "检验项目ID")
    @Column(name = "inspection_item_id")
    private Long inspectionItemId;

    /**
     * 检验工具
     */
    @ApiModelProperty(name="inspectionTool",value = "检验工具")
    @Excel(name = "检验工具", height = 20, width = 30,orderNum="5")
    @Column(name = "inspection_tool")
    private String inspectionTool;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    @Column(name = "inspection_way_id")
    private Long inspectionWayId;

    /**
     * 抽样过程
     */
    @ApiModelProperty(name="sampleProcessId",value = "抽样过程")
    @Column(name = "sample_process_id")
    private Long sampleProcessId;

    /**
     * 检验标识(1-定性 2-定量)
     */
    @ApiModelProperty(name="inspectionTag",value = "检验标识(1-定性 2-定量)")
    @Excel(name = "检验标识(1-定性 2-定量)", height = 20, width = 30,orderNum="7",replace = {"定性_1", "定量_2"})
    @Column(name = "inspection_tag")
    private Byte inspectionTag;

    /**
     * 规格上限
     */
    @ApiModelProperty(name="specificationUpperLimit",value = "规格上限")
    @Excel(name = "规格上限", height = 20, width = 30,orderNum="8")
    @Column(name = "specification_upper_limit")
    private BigDecimal specificationUpperLimit;

    /**
     * 规格下限
     */
    @ApiModelProperty(name="specificationFloor",value = "规格下限")
    @Excel(name = "规格下限", height = 20, width = 30,orderNum="9")
    @Column(name = "specification_floor")
    private BigDecimal specificationFloor;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="10")
    @Column(name = "unit_name")
    private String unitName;

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
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
    @Transient
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    /**
     * 检验项目-小类
     */
    @ApiModelProperty(name="inspectionItemDescSmall" ,value="检验项目-小类")
    @Transient
    @Excel(name = "检验项目-小类", height = 20, width = 30,orderNum="3")
    private String inspectionItemDescSmall;

    /**
     * 检验项目-大类
     */
    @ApiModelProperty(name="inspectionItemDescBig" ,value="检验项目-大类")
    @Transient
    @Excel(name = "检验项目-大类", height = 20, width = 30,orderNum="2")
    private String inspectionItemDescBig;

    /**
     * 检验标准
     */
    @ApiModelProperty(name="inspectionItemStandard" ,value="检验标准")
    @Transient
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="4")
    private String inspectionItemStandard;

    /**
     * 抽样过程
     */
    @ApiModelProperty(name="sampleProcessCode" ,value="抽样过程")
    @Transient
    @Excel(name = "抽样过程", height = 20, width = 30,orderNum="6")
    private String sampleProcessCode;

    /**
     * 抽样类型
     */
    @ApiModelProperty(name="sampleProcessType" ,value="抽样类型")
    @Transient
    private Byte sampleProcessType;

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

    private static final long serialVersionUID = 1L;
}