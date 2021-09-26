package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Data
public class InventoryAge implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(name = "inventoryId",value = "id")
    @Id
    private Long inventoryId;

    /**
     * 序号
     */
    @ApiModelProperty(name = "serialNumber",value = "序号")
    @Excel(name = "序号", height = 20, width = 30,orderNum="1")
    private int serialNumber;

    /**
     * 库存组织
     */
    @ApiModelProperty(name = "organizationName",value = "库存组织")
    @Excel(name = "库存组织", height = 20, width = 30,orderNum="2")
    private String organizationName;

    /**
     *仓库
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="3")
    private String warehouseName;

    /**
     *库区
     */
    @ApiModelProperty(name = "warehouseAreaCode",value = "库区")
    @Excel(name = "库区", height = 20, width = 30,orderNum="4")
    private String warehouseAreaCode;

    /**
     *库位
     */
    @ApiModelProperty(name = "storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="5")
    private String storageCode;

    /**
     *物料号
     */
    @ApiModelProperty(name = "materialCode",value = "物料号")
    @Excel(name = "物料号", height = 20, width = 30,orderNum="6")
    private String materialCode;

    /**
     *物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="7")
    private String materialName;

    /**
     *当前库存数量
     */
    @ApiModelProperty(name = "qty",value = "当前库存数量")
    @Excel(name = "当前库存数量", height = 20, width = 30,orderNum="8")
    private BigDecimal qty;

    /**
     *单位
     */
    @ApiModelProperty(name = "packingUnitName",value = "单位")
    @Excel(name = "单位", height = 20, width = 30,orderNum="9")
    private String  packingUnitName;
}
