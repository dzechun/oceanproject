package com.fantechs.common.base.general.entity.qms.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 检验项目历史表
 * qms_ht_inspection_item
 */
@Data
@Table(name = "qms_ht_inspection_item")
public class QmsHtInspectionItem implements Serializable {
    /**
     * 检验项目历史ID
     */
    @ApiModelProperty(name="htInspectionItemId",value = "检验项目历史ID")
    @Excel(name = "检验项目历史ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_inspection_item_id")
    private Long htInspectionItemId;

    /**
     * 检验项目ID
     */
    @ApiModelProperty(name="inspectionItemId",value = "检验项目ID")
    @Excel(name = "检验项目ID", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_item_id")
    private Long inspectionItemId;

    /**
     * 检验项目单号
     */
    @ApiModelProperty(name="inspectionItemCode",value = "检验项目单号")
    @Excel(name = "检验项目单号", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_item_code")
    private String inspectionItemCode;

    /**
     * 检验项目名称
     */
    @ApiModelProperty(name="inspectionItemName",value = "检验项目名称")
    @Excel(name = "检验项目名称", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_item_name")
    private String inspectionItemName;

    /**
     * 检验项目水平
     */
    @ApiModelProperty(name="inspectionItemLevel",value = "检验项目水平")
    @Excel(name = "检验项目水平", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_item_level")
    private Long inspectionItemLevel;

    /**
     * 检验工具
     */
    @ApiModelProperty(name="inspectionTool",value = "检验工具")
    @Excel(name = "检验工具", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_tool")
    private Long inspectionTool;

    /**
     * 检验标准
     */
    @ApiModelProperty(name="inspectionStandard",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_standard")
    private String inspectionStandard;

    /**
     * 检验项
     */
    @ApiModelProperty(name="inspectionNape",value = "检验项")
    @Excel(name = "检验项", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_nape")
    private Long inspectionNape;

    /**
     * 批量
     */
    @ApiModelProperty(name="batch",value = "批量")
    @Excel(name = "批量", height = 20, width = 30,orderNum="")
    private BigDecimal batch;

    /**
     * AQL
     */
    @ApiModelProperty(name="aql",value = "AQL")
    @Excel(name = "AQL", height = 20, width = 30,orderNum="")
    private BigDecimal aql;

    /**
     * 抽样数量
     */
    @ApiModelProperty(name="samplingAmount",value = "抽样数量")
    @Excel(name = "抽样数量", height = 20, width = 30,orderNum="")
    @Column(name = "sampling_amount")
    private BigDecimal samplingAmount;

    /**
     * AC
     */
    @ApiModelProperty(name="ac",value = "AC")
    @Excel(name = "AC", height = 20, width = 30,orderNum="")
    private BigDecimal ac;

    /**
     * RE
     */
    @ApiModelProperty(name="re",value = "RE")
    @Excel(name = "RE", height = 20, width = 30,orderNum="")
    private BigDecimal re;

    /**
     * 测试方法
     */
    @ApiModelProperty(name="testMethod",value = "测试方法")
    @Excel(name = "测试方法", height = 20, width = 30,orderNum="")
    @Column(name = "test_method")
    private Long testMethod;

    /**
     * 测试次数
     */
    @ApiModelProperty(name="testTimes",value = "测试次数")
    @Excel(name = "测试次数", height = 20, width = 30,orderNum="")
    @Column(name = "test_times")
    private BigDecimal testTimes;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="")
    private Byte status;

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

    private static final long serialVersionUID = 1L;
}
