package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class BaseStorageMaterialImport implements Serializable {

    /**
     * 库位编码(必填)
     */
    @ApiModelProperty(name = "storageCode",value = "库位编码")
    @Excel(name = "库位编码(必填)", height = 20, width = 30)
    private String storageCode;

    /**
     * 库位ID
     */
    @ApiModelProperty(name = "storageId",value = "库位ID")
    private Long storageId;

    /**
     * 物料编码(必填)
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码(必填)")
    @Excel(name = "物料编码(必填)", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "materialId",value = "物料ID")
    private Long materialId;

    /**
     * 货主编码(必填)
     */
    @ApiModelProperty(name = "materialOwnerCode",value = "货主编码(必填)")
    @Excel(name = "货主编码(必填)", height = 20, width = 30)
    private String materialOwnerCode;

    /**
     * 货主ID
     */
    @ApiModelProperty(name = "materialOwnerId",value = "货主ID")
    private Long materialOwnerId;

    /**
     * 上架策略
     */
    @ApiModelProperty(name = "putawayTactics",value = "上架策略")
    private Integer putawayTactics;

    /**
     * 补货策略
     */
    @ApiModelProperty(name = "replenishTactics",value = "补货策略")
    private Integer replenishTactics;

}
