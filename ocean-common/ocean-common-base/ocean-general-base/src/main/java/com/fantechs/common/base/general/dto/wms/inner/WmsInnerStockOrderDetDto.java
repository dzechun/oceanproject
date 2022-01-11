package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/27
 */
@Data
public class WmsInnerStockOrderDetDto extends WmsInnerStockOrderDet implements Serializable {

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="planStockOrderCode",value = "盘点单号")
    @Excel(name = "盘点计划单号", height = 20, width = 30)
    private String planStockOrderCode;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库", height = 20, width = 30)
    private String warehouseName;

    /**
     * 库位编码
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30)
    private String storageCode;

    /**
     * 库位名称
     */
    @Transient
    @ApiModelProperty(name = "storageName",value = "库位名称")
    @Excel(name = "库位名称", height = 20, width = 30)
    private String storageName;

    /**
     * 库位描述
     */
    @Transient
    @ApiModelProperty(name = "storageDesc",value = "库位描述")
    private String storageDesc;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    /**
     * 物料规格
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料规格")
    @Excel(name = "物料规格", height = 20, width = 30)
    private String materialDesc;

    /**
     * 作业员
     */
    @Transient
    @ApiModelProperty(name="workName",value = "作业员")
    @Excel(name = "作业员", height = 20, width = 30)
    private String workName;

    /**
     * 库存状态
     */
    @Transient
    @ApiModelProperty(name = "inventoryStatusName",value = "库存状态")
    @Excel(name = "库存状态", height = 20, width = 30)
    private String inventoryStatusName;

    /**
     * 供应商
     */
    @Transient
    @ApiModelProperty(name="supplierName" ,value="供应商")
    @Excel(name = "供应商", height = 20, width = 30)
    private String supplierName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;
}
