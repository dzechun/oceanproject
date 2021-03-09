package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlipDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerTransferSlipDetDto extends WmsInnerTransferSlipDet implements Serializable {

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

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     *  产品型号编码
     */
    @Transient
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Excel(name = "产品型号", height = 20, width = 30)
    private String productModelCode;

    /**
     *  产品型号名称
     */
    @Transient
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    @Excel(name = "产品型号名称", height = 20, width = 30)
    private String productModelName;

    /**
     *  产品型号描述
     */
    @Transient
    @ApiModelProperty(name="productModelDesc" ,value="产品型号描述")
    @Excel(name = "产品型号描述", height = 20, width = 30)
    private String productModelDesc;

    /**
     * 调出储位名称
     */
    @Transient
    @ApiModelProperty(name = "outStorageName",value = "调出储位名称")
    @Excel(name = "调出储位名称", height = 20, width = 30)
    private String outStorageName;

    /**
     * 调出仓库名称
     */
    @Transient
    @ApiModelProperty(name = "outWarehouseName",value = "调出仓库名称")
    @Excel(name = "调出仓库名称", height = 20, width = 30)
    private String outWarehouseName;

    /**
     * 调出仓库区域名称
     */
    @Transient
    @ApiModelProperty(name="outWarehouseAreaName" ,value="调出仓库区域名称")
    @Excel(name = "调出仓库区域名称", height = 20, width = 30)
    private String outWarehouseAreaName;

    /**
     * 调入储位名称
     */
    @Transient
    @ApiModelProperty(name = "inStorageName",value = "调入储位名称")
    @Excel(name = "调入储位名称", height = 20, width = 30)
    private String inStorageName;

    /**
     * 调入仓库名称
     */
    @Transient
    @ApiModelProperty(name = "inWarehouseName",value = "调入仓库名称")
    @Excel(name = "调入仓库名称", height = 20, width = 30)
    private String inWarehouseName;

    /**
     * 调入仓库区域名称
     */
    @Transient
    @ApiModelProperty(name="inWarehouseAreaName" ,value="调入仓库区域名称")
    @Excel(name = "调入仓库区域名称", height = 20, width = 30)
    private String inWarehouseAreaName;
}
