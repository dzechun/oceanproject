package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsInnerJobOrderDetDto extends WmsInnerJobOrderDet implements Serializable {

    /**
     * 作业单号
     */
    @Transient
    @ApiModelProperty(name = "jobOrderCode",value = "作业单号")
    @Excel(name = "作业单号", height = 20, width = 30,orderNum="1")
    private String jobOrderCode;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="2")
    private String warehouseName;


    /**
     * 移出库位
     */
    @Transient
    @ApiModelProperty(name = "outStorageName",value = "移出库位")
    @Excel(name = "移出库位", height = 20, width = 30,orderNum="3")
    private String outStorageName;

    /**
     * 移出库位编码
     */
    @Transient
    @ApiModelProperty(name = "outStorageCode",value = "移出库位编码")
    private String outStorageCode;

    /**
     * 移入库位
     */
    @Transient
    @ApiModelProperty(name = "inStorageName",value = "移入库位")
    @Excel(name = "移入库位", height = 20, width = 30,orderNum="4")
    private String inStorageName;

    /**
     * 移入库位编码
     */
    @Transient
    @ApiModelProperty(name = "inStorageCode",value = "移入库位编码")
    private String inStorageCode;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="5")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="6")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    /**
     * 库存状态
     */
    @Transient
    @ApiModelProperty(name = "inventoryStatusName",value = "库存状态")
    private String inventoryStatusName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 已打印物料总数量
     */
    @ApiModelProperty(name="totalMaterialQty",value = "已打印物料总数量")
    @Transient
    private BigDecimal totalMaterialQty;
}
