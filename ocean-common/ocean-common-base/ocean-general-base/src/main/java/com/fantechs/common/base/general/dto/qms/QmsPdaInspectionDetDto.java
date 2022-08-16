package com.fantechs.common.base.general.dto.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspectionDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @date 2021-01-07 20:01:55
 */
@Data
public class QmsPdaInspectionDetDto extends QmsPdaInspectionDet implements Serializable {

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 产品料号描述
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "产品料号描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;


    /**
     * 区域名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "区域名称")
    @Excel(name = "区域名称", height = 20, width = 30)
    private String warehouseAreaName;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30)
    private String productModelName;

    /**
     * 箱码
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "箱码")
    @Excel(name = "箱码", height = 20, width = 30)
    private String boxCode;

    /**
     * 栈板码
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "栈板码")
    @Excel(name = "栈板码", height = 20, width = 30)
    private String palletCode;

    /**
     * 总数量
     */
    @Transient
    @ApiModelProperty(name = "totalQuantity",value = "总数量")
    @Excel(name = "总数量", height = 20, width = 30)
    private BigDecimal totalQuantity;

    /**
     * 不合格数量
     */
    @Transient
    @ApiModelProperty(name = "unqualifiedQuantity",value = "不合格数量")
    @Excel(name = "总数量", height = 20, width = 30)
    private BigDecimal unqualifiedQuantity;

    /**
     * 单位
     */
    @Transient
    @ApiModelProperty(name = "unit",value = "单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;


    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="13")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="15")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
