package com.fantechs.common.base.general.entity.bcm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.general.entity.bcm.OltSafeStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/3/4
 */
@Data
public class SearchOltSafeStock extends BaseQuery implements Serializable {
    /**
     * 安全库存类型1：仓库、2:物料类型、3:产品料号
     */
    @ApiModelProperty(name="safeStockType",value = "安全库存类型1：仓库、2:物料类型、3:产品料号")
    private Byte safeStockType;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 物料类别名称
     */
    @ApiModelProperty(name="materialCategoryName",value = "物料类别名称")
    private String materialCategoryName;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    private Long materialId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;
}
