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


/**
 * 检验类型
 * @date 2020-12-23 11:09:14
 */
@Data
@Table(name = "qms_inspection_type")
public class QmsInspectionType extends ValidGroup implements Serializable {
    /**
     * 检验类型ID
     */
    @ApiModelProperty(name="inspectionTypeId",value = "检验类型ID")
    @Id
    @Column(name = "inspection_type_id")
    private Long inspectionTypeId;

    /**
     * 检验类型单号
     */
    @ApiModelProperty(name="inspectionTypeCode",value = "检验类型单号")
    @Excel(name = "检验类型单号", height = 20, width = 30,orderNum="1")
    @Column(name = "inspection_type_code")
    private String inspectionTypeCode;

    /**
     * 检验类型名称
     */
    @ApiModelProperty(name="inspectionTypeName",value = "检验类型名称")
    @Excel(name = "检验类型名称", height = 20, width = 30,orderNum="2")
    @Column(name = "inspection_type_name")
    private String inspectionTypeName;

    /**
     * 检验类型水平
     */
    @ApiModelProperty(name="inspectionTypeLevel",value = "检验类型水平")
    @Excel(name = "检验类型水平", height = 20, width = 30,orderNum="3")
    @Column(name = "inspection_type_level")
    private Long inspectionTypeLevel;

    /**
     * 检验工具
     */
    @ApiModelProperty(name="inspectionTool",value = "检验工具")
    @Excel(name = "检验工具", height = 20, width = 30,orderNum="4")
    @Column(name = "inspection_tool")
    private Long inspectionTool;

    /**
     * 检验标准
     */
    @ApiModelProperty(name="inspectionStandard",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="5")
    @Column(name = "inspection_standard")
    private String inspectionStandard;

    /**
     * 检验项
     */
    @ApiModelProperty(name="inspectionNape",value = "检验项")
    @Excel(name = "检验项", height = 20, width = 30,orderNum="6")
    @Column(name = "inspection_nape")
    private Long inspectionNape;

    /**
     * 批量
     */
    @ApiModelProperty(name="batch",value = "批量")
    @Excel(name = "批量", height = 20, width = 30,orderNum="7")
    private BigDecimal batch;

    /**
     * AQL
     */
    @ApiModelProperty(name="aql",value = "AQL")
    @Excel(name = "AQL", height = 20, width = 30,orderNum="8")
    private BigDecimal aql;

    /**
     * 抽样数量
     */
    @ApiModelProperty(name="samplingAmount",value = "抽样数量")
    @Excel(name = "抽样数量", height = 20, width = 30,orderNum="9")
    @Column(name = "sampling_amount")
    private BigDecimal samplingAmount;

    /**
     * AC
     */
    @ApiModelProperty(name="ac",value = "AC")
    @Excel(name = "AC", height = 20, width = 30,orderNum="10")
    private BigDecimal ac;

    /**
     * RE
     */
    @ApiModelProperty(name="re",value = "RE")
    @Excel(name = "RE", height = 20, width = 30,orderNum="11")
    private BigDecimal re;

    /**
     * 测试方法
     */
    @ApiModelProperty(name="testMethod",value = "测试方法")
    @Excel(name = "测试方法", height = 20, width = 30,orderNum="12")
    @Column(name = "test_method")
    private Long testMethod;

    /**
     * 测试次数
     */
    @ApiModelProperty(name="testTimes",value = "测试次数")
    @Excel(name = "测试次数", height = 20, width = 30,orderNum="13")
    @Column(name = "test_times")
    private BigDecimal testTimes;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="14")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="16",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="18",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
