package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsOutDeliveryOrderDetDto extends WmsOutDeliveryOrderDet implements Serializable {

    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    @ApiModelProperty(name="supplierName" ,value="客户名称")
    private String supplierName;

    @ApiModelProperty(name="orderCode" ,value="订单号")
    private String orderCode;

    @ApiModelProperty(name="contractCode" ,value="合同号")
    private String contractCode;

    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    @ApiModelProperty(name="productModelName" ,value="产品型号")
    private String productModelName;

    @ApiModelProperty(name="realityCartonQty" ,value="出货箱数")
    private String realityCartonQty;






}
