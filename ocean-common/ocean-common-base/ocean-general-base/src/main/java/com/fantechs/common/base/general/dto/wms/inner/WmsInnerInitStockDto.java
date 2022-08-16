package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@Data
public class WmsInnerInitStockDto extends WmsInnerInitStock implements Serializable {

    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="2")
    private String warehouseCode;

    @ApiModelProperty(name = "warehouseCode",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="3")
    private String warehouseName;

    @ApiModelProperty(name = "storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="4")
    private String storageCode;

    @ApiModelProperty(name = "totalStockQty",value = "实际盘点数量")
    @Excel(name = "实际盘点数量", height = 20, width = 30,orderNum="7")
    private BigDecimal totalStockQty;

    @ApiModelProperty(name = "totalVarianceQty",value = "差异数量")
    @Excel(name = "差异数量", height = 20, width = 30,orderNum="8")
    private BigDecimal totalVarianceQty;

    @ApiModelProperty(name = "createUserName",value = "创建用户")
    @Excel(name = "创建用户", height = 20, width = 30,orderNum="10")
    private String createUserName;

    @ApiModelProperty(name = "modifiedUserName",value = "修改用户")
    @Excel(name = "修改用户", height = 20, width = 30,orderNum="12")
    private String modifiedUserName;

    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
