package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDetBarcode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/5/27
 */
@Data
public class WmsInnerStockOrderDetBarcodeDto extends WmsInnerStockOrderDetBarcode implements Serializable {

    /**
     * 条码（SN码）
     */
    @Transient
    @ApiModelProperty(name="barcode",value = "条码（SN码）")
    private String barcode;

    /**
     * 彩盒号
     */
    @Transient
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30)
    private String colorBoxCode;

    /**
     * 箱号
     */
    @Transient
    @ApiModelProperty(name="cartonCode",value = "箱号")
    @Excel(name = "箱号", height = 20, width = 30,orderNum="7")
    private String cartonCode;

    /**
     * 栈板号
     */
    @Transient
    @ApiModelProperty(name="palletCode",value = "栈板号")
    @Excel(name = "栈板号", height = 20, width = 30,orderNum="8")
    private String palletCode;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="4")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="5")
    private String materialName;

    /**
     * 物料规格
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料规格")
    @Excel(name = "物料规格", height = 20, width = 30)
    private String materialDesc;

    /**
     * 物料数量
     */
    @Transient
    @ApiModelProperty(name="materialQty",value = "物料数量")
    @Excel(name = "物料数量", height = 20, width = 30)
    private BigDecimal materialQty;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;
}
