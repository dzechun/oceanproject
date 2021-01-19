package com.fantechs.common.base.general.dto.qms;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class QmsQualityConfirmationDto extends QmsQualityConfirmation implements Serializable {

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="2")
    private String workOrderCode;

    /**
     * 流程单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCardId",value = "流程单号")
    @Excel(name = "流程单号", height = 20, width = 30,orderNum="2")
    private String workOrderCardId;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="2")
    private String materialDesc;

    /**
     * 产品编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="2")
    private String productModelName;

    /**
     * 单位
     */
    @Transient
    @ApiModelProperty(name = "unit",value = "单位")
    @Excel(name = "单位", height = 20, width = 30,orderNum="2")
    private String unit;

    /**
     * 部件名称
     */
    @Transient
    @ApiModelProperty(name = "partsInformationName",value = "部件名称")
    @Excel(name = "部件名称", height = 20, width = 30,orderNum="2")
    private String partsInformationName;

    /**
     * 部门名称
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "部门名称")
    @Excel(name = "部门名称", height = 20, width = 30,orderNum="2")
    private String deptName;

    /**
     * 确认人员名称
     */
    @Transient
    @ApiModelProperty(name = "personName",value = "确认人员名称")
    @Excel(name = "确认人员名称", height = 20, width = 30,orderNum="2")
    private String personName;

    /**
     * 报工数量
     */
    @Transient
    @ApiModelProperty(name = "quantity",value = "报工数量")
    @Excel(name = "报工数量", height = 20, width = 30,orderNum="2")
    private BigDecimal quantity;

    /**
     * 报工工序名称
     */
    @Transient
    @ApiModelProperty(name = "personName",value = "报工工序名称")
    @Excel(name = "报工工序名称", height = 20, width = 30,orderNum="2")
    private String processName;

    /**
     * 所属工段名称
     */
    @Transient
    @ApiModelProperty(name = "sectionName",value = "所属工段名称")
    @Excel(name = "所属工段名称", height = 20, width = 30,orderNum="2")
    private String sectionName;

    /**
     * 所属工段ID
     */
    @Transient
    @ApiModelProperty(name = "sectionId",value = "所属工段ID")
    @Excel(name = "所属工段ID", height = 20, width = 30,orderNum="2")
    private Long sectionId;

    /**
     * 不良现象编码
     */
    @Transient
    @ApiModelProperty(name = "badItemCode",value = "不良现象编码")
    @Excel(name = "不良现象编码", height = 20, width = 30,orderNum="2")
    private String badItemCode;

    /**
     * 不良现象名称
     */
    @Transient
    @ApiModelProperty(name = "badPhenomenon",value = "不良现象名称")
    @Excel(name = "不良现象名称", height = 20, width = 30,orderNum="2")
    private String badPhenomenon;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
