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
    //@Excel(name = "库存组织", height = 20, width = 30,orderNum="2")
    private String organizationName;

    /**
     *仓库
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="13")
    private String warehouseName;

    /**
     *库区
     */
    @ApiModelProperty(name = "warehouseAreaCode",value = "库区")
    //@Excel(name = "库区", height = 20, width = 30,orderNum="4")
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
    @Excel(name = "库位", height = 20, width = 30,orderNum="15")
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
    @Excel(name = "物料号", height = 20, width = 30,orderNum="10")
    private String materialCode;

    /**
     *物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    //@Excel(name = "物料名称", height = 20, width = 30,orderNum="7")
    private String materialName;

    /**
     *正品库存
     */
    @ApiModelProperty(name = "qty",value = "正品库存")
    @Excel(name = "正品库存", height = 20, width = 30,orderNum="16")
    private BigDecimal qty;

    /**
     *单位
     */
    @ApiModelProperty(name = "packingUnitName",value = "单位")
    //@Excel(name = "单位", height = 20, width = 30,orderNum="9")
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
     *  销售编码
     */
    @ApiModelProperty(name = "salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30,orderNum="9")
    private String  salesCode;

    /**
     *  业务员
     */
    @ApiModelProperty(name = "salesUserName",value = "业务员")
    @Excel(name = "业务员", height = 20, width = 30,orderNum="2")
    private String  salesUserName;

    /**
     *  客户
     */
    @ApiModelProperty(name = "supplierName",value = "客户")
    @Excel(name = "客户", height = 20, width = 30,orderNum="5")
    private String  supplierName;

    /**
     *  国家
     */
    @ApiModelProperty(name = "countryName",value = "国家")
    @Excel(name = "国家", height = 20, width = 30,orderNum="4")
    private String  countryName;

    /**
     *  大区
     */
    @ApiModelProperty(name = "regionName",value = "大区")
    @Excel(name = "大区", height = 20, width = 30,orderNum="3")
    private String  regionName;

    /**
     *  品牌
     */
    @ApiModelProperty(name = "brandName",value = "品牌")
    @Excel(name = "品牌", height = 20, width = 30,orderNum="6")
    private String  brandName;

    /**
     *  产品分类
     */
    @ApiModelProperty(name = "productCategory",value = "产品分类")
    @Excel(name = "产品分类", height = 20, width = 30,orderNum="7")
    private String  productCategory;

    /**
     *  型号
     */
    @ApiModelProperty(name = "productModelName",value = "型号")
    @Excel(name = "产品分类", height = 20, width = 30,orderNum="8")
    private String  productModelName;

    /**
     *  批次号/PO号
     */
    @ApiModelProperty(name = "samePackageCode",value = "批次号/PO号")
    @Excel(name = "批次号/PO号", height = 20, width = 30,orderNum="12")
    private String  samePackageCode;

    /**
     *  产品描述
     */
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="11")
    private String  materialDesc;

    /**
     *子库
     */
    @ApiModelProperty(name = "subWarehouseName",value = "子库")
    @Excel(name = "子库", height = 20, width = 30,orderNum="14")
    private String subWarehouseName;

    /**
     * 明细数量1
     */
    @ApiModelProperty(name = "detCount1",value = "明细数量1")
    @Excel(name = "库存15天内数量", height = 20, width = 30,orderNum="17")
    private Integer detCount1;

    /**
     * 明细数量2
     */
    @ApiModelProperty(name = "detCount2",value = "明细数量2")
    @Excel(name = "库存15-30天数量", height = 20, width = 30,orderNum="18")
    private Integer detCount2;

    /**
     * 明细数量3
     */
    @ApiModelProperty(name = "detCount3",value = "明细数量3")
    @Excel(name = "库存30-45天数量", height = 20, width = 30,orderNum="19")
    private Integer detCount3;

    /**
     * 明细数量4
     */
    @ApiModelProperty(name = "detCount4",value = "明细数量4")
    @Excel(name = "库存45-60天数量", height = 20, width = 30,orderNum="20")
    private Integer detCount4;

    /**
     * 明细数量5
     */
    @ApiModelProperty(name = "detCount5",value = "明细数量5")
    @Excel(name = "库存60天以上数量", height = 20, width = 30,orderNum="21")
    private Integer detCount5;

    /**
     * 明细数量6
     */
    @ApiModelProperty(name = "detCount6",value = "明细数量6")
    @Excel(name = "库存半年以上数量", height = 20, width = 30,orderNum="22")
    private Integer detCount6;

    /**
     * 明细数量7
     */
    @ApiModelProperty(name = "detCount7",value = "明细数量7")
    @Excel(name = "库存一年以上数量", height = 20, width = 30,orderNum="23")
    private Integer detCount7;

    /**
     * 明细数量8
     */
    @ApiModelProperty(name = "detCount8",value = "明细数量8")
    private Integer detCount8;

    /**
     * 明细数量9
     */
    @ApiModelProperty(name = "detCount9",value = "明细数量9")
    private Integer detCount9;

    /**
     * 明细数量10
     */
    @ApiModelProperty(name = "detCount10",value = "明细数量10")
    private Integer detCount10;
}
