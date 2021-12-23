package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WmsInnerInventoryDto extends WmsInnerInventory implements Serializable {

    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="3")
    private String materialOwnerName;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="4")
    @Transient
    private String warehouseName;

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="5")
    @Transient
    private String storageCode;

    /**
     * 库位类型（1-存货 2-收货 3-发货）
     */
    @ApiModelProperty(name="storageType",value = "库位类型（1-存货 2-收货 3-发货）")
    @Excel(name = "库位类型（1-存货 2-收货 3-发货）", height = 20, width = 30,orderNum="10",replace = {"存货_1","收货_2","发货_3"})
    @Transient
    private Byte storageType;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="7")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="8")
    @Transient
    private String materialName;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="27")
    @Transient
    private String supplierName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum = "36")
    @Transient
    private String organizationName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum = "37")
    @Transient
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum = "35")
    @Transient
    private String modifiedUserName;

    /**
     * 库存状态名称
     */
    @ApiModelProperty(name="inventoryStatusName",value = "库存状态名称")
    @Excel(name = "库存状态名称", height = 20, width = 30,orderNum = "41")
    @Transient
    private String inventoryStatusName;

    /**
     * 分配数量
     */
    @ApiModelProperty(name="distributionQty",value = "分配数量")
    @Excel(name = "分配数量", height = 20, width = 30,orderNum="")
    @Transient
    private BigDecimal distributionQty;

    private static final long serialVersionUID = 1L;
}
