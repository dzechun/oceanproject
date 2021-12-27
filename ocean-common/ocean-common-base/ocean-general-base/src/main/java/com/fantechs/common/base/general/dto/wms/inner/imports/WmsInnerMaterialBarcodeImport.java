package com.fantechs.common.base.general.dto.wms.inner.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class WmsInnerMaterialBarcodeImport implements Serializable {

    /**
     * 单号
     */
    @Excel(name = "单号",  height = 20, width = 30, orderNum="1")
    @ApiModelProperty(name="orderCode" ,value="单号")
    private String orderCode;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码",  height = 20, width = 30, orderNum="2")
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 条码（SN码）
     */
    @ApiModelProperty(name="barcode",value = "条码（SN码）")
    @Excel(name = "条码（SN码）", height = 20, width = 30,orderNum="3")
    private String barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30,orderNum="4")
    private String colorBoxCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    @Excel(name = "箱号", height = 20, width = 30,orderNum="5")
    private String cartonCode;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    @Excel(name = "栈板号", height = 20, width = 30,orderNum="6")
    private String palletCode;

    /**
     * 是否系统条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifSysBarcode",value = "是否系统条码(0-否 1-是)")
    @Excel(name = "是否系统条码", height = 20, width = 30,orderNum="7",replace = {"否_0","是_1"})
    private Integer ifSysBarcode;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    @Excel(name = "批号", height = 20, width = 30,orderNum="8")
    private String batchCode;

    /**
     * 生产时间
     */
    @ApiModelProperty(name="productionTime",value = "生产时间")
    @Excel(name = "生产时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date productionTime;

    /**
     * 物料数量
     */
    @ApiModelProperty(name="materialQty",value = "物料数量")
    @Excel(name = "物料数量", height = 20, width = 30,orderNum="10")
    private BigDecimal materialQty;

    private List<WmsInnerMaterialBarcodeImport> importList;

    private List<WmsInnerMaterialBarcodeDto> list;

}
