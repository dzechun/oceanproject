package com.fantechs.common.base.general.dto.wms.out;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturnDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class WmsOutPurchaseReturnDetDto extends WmsOutPurchaseReturnDet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购退货单ID
     */
    @ApiModelProperty(name="purchaseReturnCode",value = "采购退货单号")
    @Excel(name = "采购退货单号", height = 20, width = 30,orderNum="1")
    private String purchaseReturnCode;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="8")
    private String warehouseCode;

    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="9")
    private String warehouseName;

    @ApiModelProperty(name="warehouseUserName" ,value="仓库管理员名称")
    @Excel(name = "仓库管理员名称", height = 20, width = 30,orderNum="10")
    private String warehouseUserName;

}
