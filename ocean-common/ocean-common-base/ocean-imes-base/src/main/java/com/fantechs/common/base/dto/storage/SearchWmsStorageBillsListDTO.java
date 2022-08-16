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
    @ApiModelProperty(value = "仓库清单单号")
    private String storageBillsCode;
    @ApiModelProperty(value = "仓库清单类型（1、入库计划 2、收获计划 3、完工入库计划 4、销售退货计划）")
    private Integer billsType;
    @ApiModelProperty(value = "仓库清单单据类型（1、收货单 2、入库任务单 3、完工入库单 4、销售退货单）")
    private Integer type;
    @ApiModelProperty(value = "储位条码")
    private String storageBarcode;
    @ApiModelProperty(value = "物料条码")
    private String materialBarcode;
    @ApiModelProperty(value = "仓库条码")
    private String wareHouseBarcode;
    @ApiModelProperty(value = "采购订单号（暂定）")
    private String str;
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "仓库清单状态（1、待完成 2、进行中 3、完成 4、未完成 5、待完成和进行中）")
    private Byte status;
}
