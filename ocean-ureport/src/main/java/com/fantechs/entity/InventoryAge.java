package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
     *库位id
     */
    @ApiModelProperty(name = "storageId",value = "库位id")
    private Long  storageId;

    /**
     *库位
     */
    @ApiModelProperty(name = "storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="5")
    private String storageCode;

    /**
     *物料id
     */
    @ApiModelProperty(name = "materialId",value = "物料id")
    private Long  materialId;

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

    /**
     *库存状态id
     */
    @ApiModelProperty(name = "inventoryStatusId",value = "库存状态id")
    private Long  inventoryStatusId;

    /**
     *库存状态名称
     */
    @ApiModelProperty(name = "inventoryStatusName",value = "库存状态名称")
    private String  inventoryStatusName;

    /**
     * 明细数量1
     */
    @ApiModelProperty(name = "detCount1",value = "明细数量1")
    @Excel(name = "库存1-7天数量", height = 20, width = 30,orderNum="10")
    private Integer detCount1;

    /**
     * 明细数量2
     */
    @ApiModelProperty(name = "detCount2",value = "明细数量2")
    @Excel(name = "库存8-14天数量", height = 20, width = 30,orderNum="11")
    private Integer detCount2;

    /**
     * 明细数量3
     */
    @ApiModelProperty(name = "detCount3",value = "明细数量3")
    @Excel(name = "库存15-30天数量", height = 20, width = 30,orderNum="12")
    private Integer detCount3;

    /**
     * 明细数量4
     */
    @ApiModelProperty(name = "detCount4",value = "明细数量4")
    @Excel(name = "库存31-90天数量", height = 20, width = 30,orderNum="13")
    private Integer detCount4;

    /**
     * 明细数量5
     */
    @ApiModelProperty(name = "detCount5",value = "明细数量5")
    @Excel(name = "库存91天以上数量", height = 20, width = 30,orderNum="14")
    private Integer detCount5;
}
