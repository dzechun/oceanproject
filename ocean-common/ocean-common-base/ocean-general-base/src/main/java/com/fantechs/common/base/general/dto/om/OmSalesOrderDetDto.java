package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OmSalesOrderDetDto extends OmSalesOrderDet implements Serializable {
    /**
     *销售订单号
     */
    @Transient
    @ApiModelProperty(name = "salesOrderCode", value = "销售订单号")
    @Excel(name = "销售订单号", height = 20, width = 30)
    private String salesOrderCode;

    @Transient
    @ApiModelProperty(name = "materialCode", value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;
    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName", value = "物料名称")
    @Excel(name="物料名称" , height = 20, width = 30)
    private String materialName;

    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion", value = "物料版本")
    @Excel(name = "版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc", value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 仓库名
     */
    @Transient
    @ApiModelProperty(name="warehouseName", value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30)
    private String warehouseName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 下发数量
     */
    @ApiModelProperty(name="issueQty",value = "下发数量")
    @Transient
    private BigDecimal issueQty;

}
