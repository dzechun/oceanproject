package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrapDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class WmsInnerInventoryScrapDetDto extends WmsInnerInventoryScrapDet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码（产品料号）")
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc",value = "物料描述（产品料号描述）")
    private String materialDesc;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="version",value = "物料版本（产品料号版本）")
    private String version;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName",value = "仓库区域名称")
    private String warehouseAreaName;

    /**
     * 储位名称
     */
    @ApiModelProperty(name="storageName",value = "储位名称")
    private String storageName;




}
