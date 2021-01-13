package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsOutFinishedProductDetDto extends WmsOutFinishedProductDet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "成品出库单号", height = 20, width = 30,orderNum="1")
    @ApiModelProperty(name="finishedProductCode",value = "成品出库单号")
    private String finishedProductCode;

    @Excel(name = "成品编码", height = 20, width = 30,orderNum="2")
    @ApiModelProperty(name="productModelCode" ,value="成品编码")
    private String productModelCode;

    @Excel(name = "成品名称", height = 20, width = 30,orderNum="3")
    @ApiModelProperty(name="productModelName" ,value="成品名称")
    private String productModelName;

    @Excel(name = "成品描述", height = 20, width = 30,orderNum="4")
    @ApiModelProperty(name="productModelDesc" ,value="成品描述（规格？）")
    private String productModelDesc;

    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="6")
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    private String warehouseCode;

    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="7")
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    @Excel(name = "仓库管理员名称", height = 20, width = 30,orderNum="8")
    @ApiModelProperty(name="warehouseUserName" ,value="仓库管理员名称")
    private String warehouseUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
