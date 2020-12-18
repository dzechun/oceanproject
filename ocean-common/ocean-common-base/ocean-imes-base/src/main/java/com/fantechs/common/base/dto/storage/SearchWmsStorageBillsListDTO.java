package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 仓库清单表搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchWmsStorageBillsListDTO extends BaseQuery implements Serializable {
    @ApiModelProperty(value = "单号")
    private String storageBillsCode;
    @ApiModelProperty(value = "单据类型（1、入库计划 2、收获计划 3、完工入库计划 4、销售退货计划）")
    private Integer type;
    @ApiModelProperty(value = "储位条码")
    private String storageBarcode;
    @ApiModelProperty(value = "物料条码")
    private String materialBarcode;
}
