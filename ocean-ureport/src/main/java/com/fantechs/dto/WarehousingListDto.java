package com.fantechs.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WarehousingListDto implements Serializable {

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

    @ApiModelProperty(name="barcode" ,value="厂内码")
    @Excel(name = "厂内码", height = 20, width = 30)
    private String barcode;

    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    @Excel(name = "销售条码", height = 20, width = 30)
    private String salesBarcode;

    @ApiModelProperty(name = "customerBarcode",value = "客户条码")
    @Excel(name = "客户条码", height = 20, width = 30)
    private String customerBarcode;

    @ApiModelProperty(name="cartonTime",value = "打包时间")
    @Excel(name = "打包时间", height = 20, width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date cartonTime;

    @ApiModelProperty(name = "cartonStatus",value = "打包状态(0OK,1NG)")
    @Excel(name = "打包状态", height = 20, width = 30, replace = {"OK_0", "NG_1"})
    private Byte cartonStatus;

    @ApiModelProperty(name = "cartonCount",value = "打包数量")
    @Excel(name = "打包数量", height = 20, width = 30)
    private BigDecimal cartonCount;

    @ApiModelProperty(name="palletTime",value = "入库下线时间")
    @Excel(name = "入库下线时间", height = 20, width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date palletTime;

    @ApiModelProperty(name = "palletStatus",value = "入库下线状态(0OK,1NG)")
    @Excel(name = "入库下线状态", height = 20, width = 30, replace = {"OK_0", "NG_1"})
    private Byte palletStatus;

    @ApiModelProperty(name = "palletCount",value = "入库下线数量")
    @Excel(name = "入库下线数量", height = 20, width = 30)
    private BigDecimal palletCount;

    @ApiModelProperty(name="inventotyTime",value = "入库时间")
    @Excel(name = "入库时间", height = 20, width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date inventotyTime;

    @ApiModelProperty(name = "inventotyStatus",value = "入库状态(0OK,1NG)")
    @Excel(name = "入库状态", height = 20, width = 30, replace = {"OK_0", "NG_1"})
    private Byte inventotyStatus;

    @ApiModelProperty(name = "inventotyCount",value = "入库数量")
    @Excel(name = "入库数量", height = 20, width = 30)
    private BigDecimal inventotyCount;

    @ApiModelProperty(name = "storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30)
    private String storageCode;

    @ApiModelProperty(name="siteStatus",value = "状态(1未过站，2已打包未入库，3已入库未上架，4正常)")
    @Excel(name = "状态", height = 20, width = 30, replace = {"未过站_1", "已打包未入库_2", "已入库未上架_3", "正常_4"})
    private Byte siteStatus;

}
