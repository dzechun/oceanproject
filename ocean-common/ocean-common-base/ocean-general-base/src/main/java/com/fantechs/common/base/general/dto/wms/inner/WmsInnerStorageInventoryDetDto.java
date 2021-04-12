package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Date 2020/12/4 14:43
 */
@Data
public class WmsInnerStorageInventoryDetDto extends WmsInnerStorageInventoryDet implements Serializable {

    /**
     * 储位Id
     */
    @Transient
    @ApiModelProperty(name = "storageId",value = "储位Id")
    @Excel(name = "储位Id", height = 20, width = 30)
    private String storageId;

    /**
     * 储位编码
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30)
    private String storageCode;

    /**
     * 储位名称
     */
    @Transient
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30)
    private String storageName;

    /**
     * 仓库区域Id
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域Id")
    @Excel(name = "仓库区域Id", height = 20, width = 30)
    private String warehouseAreaId;

    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30)
    private String warehouseAreaName;

    /**
     * 仓库Id
     */
    @Transient
    @ApiModelProperty(name = "warehouseId",value = "仓库Id")
    @Excel(name = "仓库Id", height = 20, width = 30)
    private String warehouseId;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30)
    private String warehouseName;

    /**
     * 物料Id
     */
    @Transient
    @ApiModelProperty(name="materialId" ,value="物料Id")
    @Excel(name = "物料Id", height = 20, width = 30)
    private Long materialId;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30)
    private String version;

    /**
     * 箱数
     */
    @Transient
    @ApiModelProperty(name="total" ,value="箱数")
    @Excel(name = "箱数", height = 20, width = 30)
    private BigDecimal total;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;
}
