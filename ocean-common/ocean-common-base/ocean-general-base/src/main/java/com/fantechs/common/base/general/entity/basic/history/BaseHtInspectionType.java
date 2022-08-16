package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
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

/**
 * 检验类型历史
 * qms_ht_inspection_type
 */
@Data
@Table(name = "base_ht_inspection_type")
public class BaseHtInspectionType implements Serializable {
    /**
     * 检验类型历史ID
     */
    @ApiModelProperty(name="htInspectionTypeId",value = "检验类型历史ID")
    @Excel(name = "检验类型历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_inspection_type_id")
    private Long htInspectionTypeId;

    /**
     * 检验类型ID
     */
    @ApiModelProperty(name="inspectionTypeId",value = "检验类型ID")
    @Excel(name = "检验类型ID", height = 20, width = 30)
    @Column(name = "inspection_type_id")
    private Long inspectionTypeId;

    /**
     * 检验类型单号
     */
    @ApiModelProperty(name="inspectionTypeCode",value = "检验类型单号")
    @Excel(name = "检验类型单号", height = 20, width = 30)
    @Column(name = "inspection_type_code")
    private String inspectionTypeCode;

    /**
     * 检验类型名称
     */
    @ApiModelProperty(name="inspectionTypeName",value = "检验类型名称")
    @Excel(name = "检验类型名称", height = 20, width = 30)
    @Column(name = "inspection_type_name")
    private String inspectionTypeName;

    /**
     * 检验类型水平
     */
    @ApiModelProperty(name="inspectionTypeLevel",value = "检验类型水平")
    @Excel(name = "检验类型水平", height = 20, width = 30)
    @Column(name = "inspection_type_level")
    private Long inspectionTypeLevel;

    /**
     * 检验工具
     */
    @ApiModelProperty(name="inspectionTool",value = "检验工具")
    @Excel(name = "检验工具", height = 20, width = 30)
    @Column(name = "inspection_tool")
    private Long inspectionTool;

    /**
     * 检验标准
     */
    @ApiModelProperty(name="inspectionStandard",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30)
    @Column(name = "inspection_standard")
    private String inspectionStandard;

    /**
     * 检验项
     */
    @ApiModelProperty(name="inspectionNape",value = "检验项")
    @Excel(name = "检验项", height = 20, width = 30)
    @Column(name = "inspection_nape")
    private Long inspectionNape;

    /**
     * 批量
     */
    @ApiModelProperty(name="batch",value = "批量")
    @Excel(name = "批量", height = 20, width = 30)
    private BigDecimal batch;

    /**
     * AQL
     */
    @ApiModelProperty(name="aql",value = "AQL")
    @Excel(name = "AQL", height = 20, width = 30)
    private BigDecimal aql;

    /**
     * 抽样数量
     */
    @ApiModelProperty(name="samplingAmount",value = "抽样数量")
    @Excel(name = "抽样数量", height = 20, width = 30)
    @Column(name = "sampling_amount")
    private BigDecimal samplingAmount;

    /**
     * AC
     */
    @ApiModelProperty(name="ac",value = "AC")
    @Excel(name = "AC", height = 20, width = 30)
    private BigDecimal ac;

    /**
     * RE
     */
    @ApiModelProperty(name="re",value = "RE")
    @Excel(name = "RE", height = 20, width = 30)
    private BigDecimal re;

    /**
     * 测试方法
     */
    @ApiModelProperty(name="testMethod",value = "测试方法")
    @Excel(name = "测试方法", height = 20, width = 30)
    @Column(name = "test_method")
    private Long testMethod;

    /**
     * 测试次数
     */
    @ApiModelProperty(name="testTimes",value = "测试次数")
    @Excel(name = "测试次数", height = 20, width = 30)
    @Column(name = "test_times")
    private BigDecimal testTimes;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
