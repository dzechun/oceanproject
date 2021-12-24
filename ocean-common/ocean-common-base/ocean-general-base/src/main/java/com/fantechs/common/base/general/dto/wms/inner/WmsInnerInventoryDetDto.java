package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/2
 */
@Data
public class WmsInnerInventoryDetDto extends WmsInnerInventoryDet implements Serializable {
    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="1")
    @Transient
    private String storageCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Transient
    private String warehouseName;

    /**
     * 仓库
     */
    @ApiModelProperty(name="warehouseId",value = "仓库")
    @Transient
    private Long warehouseId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    @Transient
    private String materialCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Transient
        private Long materialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    @Transient
    private String materialName;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="4")
    @Transient
    private String supplierName;

    /**
     * 库存状态名称
     */
    @ApiModelProperty(name="inventoryStatusName",value = "库存状态名称")
    @Transient
    private String inventoryStatusName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum = "16")
    @Transient
    private String organizationName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum = "17")
    @Transient
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum = "19")
    @Transient
    private String modifiedUserName;

    /**
     * 同包装编码
     */
    @ApiModelProperty(name="samePackageCode",value = "同包装编码")
    @Transient
    private String samePackageCode;


    /**
     * 条码（SN码）
     */
    @ApiModelProperty(name="barcode",value = "条码（SN码）")
    private String barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    private String colorBoxCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    private String cartonCode;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    private String palletCode;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    /**
     * 物料数量
     */
    @ApiModelProperty(name="materialQty",value = "物料数量")
    private BigDecimal materialQty;
}
