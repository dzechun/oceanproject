package com.fantechs.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Date 2021/3/18 15:46
 */
@Data
public class QaInspectionCondition  extends ValidGroup implements Serializable {

    @Id
    private Long id;

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractCode", value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30)
    private String contractCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name = "supplierName", value = "客户名称")
    @Excel(name = "客户名称", height = 20, width = 30)
    private String supplierName;

    /**
     * 质检单
     */
    @ApiModelProperty(name = "pdaInspectionCode", value = "质检单")
    @Excel(name = "质检单", height = 20, width = 30)
    private String pdaInspectionCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name = "workOrderCode", value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30)
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name = "materialCode", value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 产品型号
     */
    @ApiModelProperty(name = "productModelName", value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30)
    private String productModelName;

    /**
     * 产品料号描述
     */
    @ApiModelProperty(name = "materialDesc", value = "产品料号描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 栈板号
     */
    @ApiModelProperty(name = "palletCode", value = "栈板号")
    @Excel(name = "栈板号", height = 20, width = 30)
    private String palletCode;

    /**
     * 栈板总数
     */
    @ApiModelProperty(name = "palletTotal", value = "栈板总数")
    @Excel(name = "栈板总数", height = 20, width = 30)
    private BigDecimal palletTotal;

    /**
     * 检验数量
     */
    @ApiModelProperty(name = "inspectionQuantity", value = "检验数量")
    @Excel(name = "检验数量", height = 20, width = 30)
    private BigDecimal inspectionQuantity;

    /**
     * 合格数量
     */
    @ApiModelProperty(name = "qualifiedQuantity", value = "合格数量")
    @Excel(name = "合格数量", height = 20, width = 30)
    private BigDecimal qualifiedQuantity;

    /**
     * 抽检日期
     */
    @ApiModelProperty(name = "inspectionTime", value = "抽检日期")
    @Excel(name = "抽检日期", height = 20, width = 30)
    private Date inspectionTime;

    /**
     * 检验人
     */
    @ApiModelProperty(name = "surveyor", value = "检验人")
    @Excel(name = "检验人", height = 20, width = 30)
    private String surveyor;

    /**
     * 抽检情况
     */
    @ApiModelProperty(name = "inspectionResult", value = "抽检情况")
    @Excel(name = "抽检情况", height = 20, width = 30)
    private String inspectionResult;

    /**
     * 整改日期
     */
    @ApiModelProperty(name = "feedbackTime", value = "整改日期")
    @Excel(name = "整改日期", height = 20, width = 30)
    private Date feedbackTime;

    /**
     * 整改人
     */
    @ApiModelProperty(name = "feedbackUserName", value = "整改人")
    @Excel(name = "整改人", height = 20, width = 30)
    private String feedbackUserName;

    /**
     * 整改事项
     */
    @ApiModelProperty(name = "feedbackMatters", value = "整改事项")
    @Excel(name = "整改事项", height = 20, width = 30)
    private String feedbackMatters;


}
