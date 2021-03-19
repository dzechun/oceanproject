package com.fantechs.dto;

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
    private String contractCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name = "supplierName", value = "客户名称")
    private String supplierName;

    /**
     * 质检单
     */
    @ApiModelProperty(name = "pdaInspectionCode", value = "质检单")
    private String pdaInspectionCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name = "workOrderCode", value = "工单号")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name = "materialCode", value = "产品料号")
    private String materialCode;

    /**
     * 产品型号
     */
    @ApiModelProperty(name = "productModelName", value = "产品型号")
    private String productModelName;

    /**
     * 产品料号描述
     */
    @ApiModelProperty(name = "materialDesc", value = "产品料号描述")
    private String materialDesc;

    /**
     * 栈板号
     */
    @ApiModelProperty(name = "palletCode", value = "栈板号")
    private String palletCode;

    /**
     * 栈板总数
     */
    @ApiModelProperty(name = "palletTotal", value = "栈板总数")
    private BigDecimal palletTotal;

    /**
     * 检验数量
     */
    @ApiModelProperty(name = "inspectionQuantity", value = "检验数量")
    private BigDecimal inspectionQuantity;

    /**
     * 合格数量
     */
    @ApiModelProperty(name = "qualifiedQuantity", value = "合格数量")
    private BigDecimal qualifiedQuantity;

    /**
     * 抽检日期
     */
    @ApiModelProperty(name = "inspectionTime", value = "抽检日期")
    private Date inspectionTime;

    /**
     * 检验人
     */
    @ApiModelProperty(name = "surveyor", value = "检验人")
    private String surveyor;

    /**
     * 抽检情况
     */
    @ApiModelProperty(name = "inspectionResult", value = "抽检情况")
    private String inspectionResult;

    /**
     * 整改日期
     */
    @ApiModelProperty(name = "feedbackTime", value = "整改日期")
    private Date feedbackTime;

    /**
     * 整改人
     */
    @ApiModelProperty(name = "feedbackUserName", value = "整改人")
    private String feedbackUserName;

    /**
     * 整改事项
     */
    @ApiModelProperty(name = "feedbackMatters", value = "整改事项")
    private String feedbackMatters;


}
