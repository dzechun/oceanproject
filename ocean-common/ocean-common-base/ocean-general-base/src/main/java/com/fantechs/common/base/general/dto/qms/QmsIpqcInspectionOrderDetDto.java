package com.fantechs.common.base.general.dto.qms;

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
 * IPQC检验单明细
 */
@Data
public class QmsIpqcInspectionOrderDetDto implements Serializable {
    /**
     * IPQC检验单明细ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderDetId",value = "IPQC检验单明细ID")
    @Excel(name = "IPQC检验单明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ipqc_inspection_order_det_id")
    private Long ipqcInspectionOrderDetId;

    /**
     * IPQC检验单ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderId",value = "IPQC检验单ID")
    @Excel(name = "IPQC检验单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "ipqc_inspection_order_id")
    private Long ipqcInspectionOrderId;

    /**
     * 抽样标准明细ID
     */
    @ApiModelProperty(name="inspectionStandardDetId",value = "抽样标准明细ID")
    @Excel(name = "抽样标准明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_standard_det_id")
    private Long inspectionStandardDetId;

    /**
     * 不良数量
     */
    @ApiModelProperty(name="badnessQty",value = "不良数量")
    @Excel(name = "不良数量", height = 20, width = 30,orderNum="")
    @Column(name = "badness_qty")
    private Long badnessQty;

    /**
     * 检验结果(0-不合格 1-合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-不合格 1-合格)")
    @Excel(name = "检验结果(0-不合格 1-合格)", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_result")
    private Byte inspectionResult;

    /**
     * 检验时间
     */
    @ApiModelProperty(name="inspectionTime",value = "检验时间")
    @Excel(name = "检验时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "inspection_time")
    private Date inspectionTime;

    /**
     * 不良类别ID
     */
    @ApiModelProperty(name="badnessCategoryId",value = "不良类别ID")
    @Excel(name = "不良类别ID", height = 20, width = 30,orderNum="")
    @Column(name = "badness_category_id")
    private Long badnessCategoryId;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="11")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="13")
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
    @Transient
    @ApiModelProperty(name = "ifMustInspection",value = "是否必检(0-否 1-是)")
    @Excel(name = "是否必检(0-否 1-是)", height = 20, width = 30,orderNum="1")
    private Byte ifMustInspection;

    /**
     * 检验项目
     */
    @Transient
    @ApiModelProperty(name = "inspectionItem",value = "检验项目")
    @Excel(name = "检验项目", height = 20, width = 30,orderNum="2")
    private String inspectionItem;

    /**
     * 检验标识(1-定性 2-定量)
     */
    @Transient
    @ApiModelProperty(name = "inspectionTag",value = "检验标识(1-定性 2-定量)")
    @Excel(name = "检验标识(1-定性 2-定量)", height = 20, width = 30,orderNum="3")
    private Byte inspectionTag;

    /**
     * 规格上限
     */
    @Transient
    @ApiModelProperty(name = "specificationUpperLimit",value = "规格上限")
    @Excel(name = "规格上限", height = 20, width = 30,orderNum="5")
    private BigDecimal specificationUpperLimit;

    /**
     * 规格下限
     */
    @Transient
    @ApiModelProperty(name = "specificationFloor",value = "规格下限")
    @Excel(name = "规格下限", height = 20, width = 30,orderNum="6")
    private BigDecimal specificationFloor;

    /**
     * 单位名称
     */
    @Transient
    @ApiModelProperty(name = "unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="7")
    private String unitName;

    /**
     * AQL值
     */
    @Transient
    @ApiModelProperty(name = "aqlValue",value = "AQL值")
    @Excel(name = "AQL值", height = 20, width = 30,orderNum="8")
    private BigDecimal aqlValue;

    /**
     * AC值
     */
    @Transient
    @ApiModelProperty(name = "acValue",value = "AC值")
    @Excel(name = "AC值", height = 20, width = 30,orderNum="9")
    private Integer acValue;

    /**
     * RE值
     */
    @Transient
    @ApiModelProperty(name = "reValue",value = "RE值")
    @Excel(name = "RE值", height = 20, width = 30,orderNum="10")
    private Integer reValue;

    /**
     * 样本数
     */
    @Transient
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
    @ApiModelProperty(name="inspectionItemDescSmall" ,value="检验项目-小类")
    @Transient
    @Excel(name = "检验项目-小类", height = 20, width = 30,orderNum="5")
    private String inspectionItemDescSmall;

    /**
     * 检验项目-大类
     */
    @ApiModelProperty(name="inspectionItemDescBig" ,value="检验项目-大类")
    @Transient
    @Excel(name = "检验项目-大类", height = 20, width = 30,orderNum="5")
    private String inspectionItemDescBig;

    /**
     * 检验标准
     */
    @ApiModelProperty(name="inspectionItemStandard" ,value="检验标准")
    @Transient
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="5")
    private String inspectionItemStandard;

    /**
     * 不良类别编码
     */
    @ApiModelProperty(name="badnessCategoryCode" ,value="不良类别编码")
    @Transient
    @Excel(name = "不良类别编码", height = 20, width = 30,orderNum="5")
    private String badnessCategoryCode;

    /**
     * 不良类别描述
     */
    @ApiModelProperty(name="badnessCategoryDesc" ,value="不良类别描述")
    @Transient
    @Excel(name = "不良类别描述", height = 20, width = 30,orderNum="5")
    private String badnessCategoryDesc;


    /**
     * 样本值
     */
    @ApiModelProperty(name="maxSampleValue",value = "最大样本值")
    @Transient
    private String maxSampleValue;

    /**
     * 检验部门（更新人员部门）
     */
    @ApiModelProperty(name="deptName",value = "检验部门")
    @Transient
    private String deptName;


    /**
     * 样本值
     */
    @ApiModelProperty(name="minSampleValue",value = "最小样本值")
    @Transient
    private String minSampleValue;


    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}