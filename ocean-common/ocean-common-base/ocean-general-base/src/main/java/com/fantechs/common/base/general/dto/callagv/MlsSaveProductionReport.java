package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MlsSaveProductionReport {

    @ApiModelProperty(name = "inStorageCode", value = "入库库位编码")
    private String inStorageCode;

    @ApiModelProperty(name = "containCode", value = "容器编码")
    private String containCode;

    @ApiModelProperty(name = "warehouseAreaCode", value = "容器编码")
    private String warehouseAreaCode;

    @ApiModelProperty(name = "deptCodes", value = "部门")
    private String deptCodes;

    @ApiModelProperty(name = "factoryCode", value = "工厂编码")
    private String factoryCode;

    @ApiModelProperty(name = "createUserName", value = "创建人")
    private String createUserName;

    @ApiModelProperty(name = "pkGroup", value = "集团")
    private String pkGroup;

    @ApiModelProperty(name = "pkOrg", value = "库存组织")
    private String pkOrg;

    @ApiModelProperty(name = "userName", value = "工号")
    private String userName;

    @ApiModelProperty(name = "warehouseCode", value = "入库ERP仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name = "note", value = "备注")
    private String note;

    @ApiModelProperty(name = "goodsDetails", value = "出入库商品明细")
    private List<GoodsDetail> goodsDetails;
}
