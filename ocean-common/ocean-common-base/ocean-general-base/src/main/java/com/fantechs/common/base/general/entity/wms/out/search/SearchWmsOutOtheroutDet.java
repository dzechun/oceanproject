package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SearchWmsOutOtheroutDet extends BaseQuery implements Serializable {

    /**
     * 其他出库单ID
     */
    @ApiModelProperty(name="otheroutId",value = "其他出库单ID")
    private Long otheroutId;

    /**
     * 其他出库单号
     */
    @ApiModelProperty(name="otheroutCode",value = "其他出库单号")
    private String otheroutCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 仓库名称（出货仓库）
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称（出货仓库）")
    private String warehouseName;

    /**
     * 仓库编码（出货仓库）
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码（出货仓库）")
    private String warehouseCode;
}
