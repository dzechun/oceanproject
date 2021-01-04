package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 检验项目表
 * @date 2020-12-25 13:42:51
 */
@Data
@Table(name = "qms_inspection_item")
public class QmsInspectionItem extends ValidGroup implements Serializable {
    /**
     * 检验项目ID
     */
    @ApiModelProperty(name="inspectionItemId",value = "检验项目ID")
    @Id
    @Column(name = "inspection_item_id")
    private Long inspectionItemId;

    /**
     * 检验项目单号
     */
    @ApiModelProperty(name="inspectionItemCode",value = "检验项目单号")
    @Excel(name = "检验项目单号", height = 20, width = 30,orderNum="1")
    @Column(name = "inspection_item_code")
    private String inspectionItemCode;

    /**
     * 检验项目名称
     */
    @ApiModelProperty(name="inspectionItemName",value = "检验项目名称")
    @Excel(name = "检验项目名称", height = 20, width = 30,orderNum="2")
    @Column(name = "inspection_item_name")
    @NotBlank(message = "质检项目名称不能为空")
    private String inspectionItemName;

    /**
     * 检验项目水平
     */
    @ApiModelProperty(name="inspectionItemLevel",value = "检验项目水平")
    @Excel(name = "检验项目水平", height = 20, width = 30,orderNum="3")
    @Column(name = "inspection_item_level")
    @NotNull(message = "检验项目水平不能为空")
    private Long inspectionItemLevel;

    /**
     * 检验工具
     */
    @ApiModelProperty(name="inspectionTool",value = "检验工具")
    @Excel(name = "检验工具", height = 20, width = 30,orderNum="4")
    @Column(name = "inspection_tool")
    @NotNull(message = "检验工具不能为空")
    private Long inspectionTool;

    /**
     * 检验标准
     */
    @ApiModelProperty(name="inspectionStandard",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="5")
    @Column(name = "inspection_standard")
    @NotBlank(message = "检验标准不能为空")
    private String inspectionStandard;

    /**
     * 检验项
     */
    @ApiModelProperty(name="inspectionNape",value = "检验项")
    @Excel(name = "检验项", height = 20, width = 30,orderNum="6")
    @Column(name = "inspection_nape")
    @NotNull(message = "检验项不能为空")
    private Long inspectionNape;

    /**
     * 批量
     */
    @ApiModelProperty(name="batch",value = "批量")
    @Excel(name = "批量", height = 20, width = 30,orderNum="7")
    @NotNull(message = "批量不能为空")
    private BigDecimal batch;

    /**
     * AQL
     */
    @ApiModelProperty(name="aql",value = "AQL")
    @Excel(name = "AQL", height = 20, width = 30,orderNum="8")
    @NotNull(message = "AQL不能为空")
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
    @NotNull(message = "测试方法不能为空")
    private Long testMethod;

    /**
     * 测试次数
     */
    @ApiModelProperty(name="testTimes",value = "测试次数")
    @Excel(name = "测试次数", height = 20, width = 30,orderNum="13")
    @Column(name = "test_times")
    @NotNull(message = "测试次数不能为空")
    private BigDecimal testTimes;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
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
