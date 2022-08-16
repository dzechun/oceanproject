package com.fantechs.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WarehousingSummaryDto implements Serializable {

    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30)
    private String salesCode;

    @ApiModelProperty(name = "workShopName",value = "车间")
    @Excel(name = "车间", height = 20, width = 30)
    private String workShopName;

    @ApiModelProperty(name = "proName",value = "产线")
    @Excel(name = "产线", height = 20, width = 30)
    private String proName;

    @ApiModelProperty(name = "site",value = "站点(1包装、2入库、3上架、4未投产)")
    @Excel(name = "站点", height = 20, width = 30, replace = {"包装_1", "入库_2", "上架_3", "未投产_4"})
    private Byte site;

    @ApiModelProperty(name = "qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30)
    private BigDecimal qty;

    @ApiModelProperty(name = "time",value = "时间")
    @Excel(name = "时间", height = 20, width = 30, exportFormat = "yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd")
    private Date time;

    @ApiModelProperty(name="passStatus",value = "状态(0OK,1NG)")
    @Excel(name = "状态", height = 20, width = 30, replace = {"OK_0", "NG_1"})
    private Byte passStatus;

}
