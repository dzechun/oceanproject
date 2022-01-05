package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/30
 */
@Data
public class BaseInAndOutRuleImport implements Serializable {

    @Excel(name = "仓库编码", height = 20, width = 30)
    private String warehouseCode;

    @Excel(name = "仓库名称", height = 20, width = 30)
    private String warehouseName;

    /**
     * 类型(1-入库 2-出库 3-批次)
     */
    @ApiModelProperty(name="category",value = "类型(1-入库 2-出库 3-批次)")
    @Excel(name = "类型(入库 、出库 、批次)", height = 20, width = 30)
    private String category;
}
