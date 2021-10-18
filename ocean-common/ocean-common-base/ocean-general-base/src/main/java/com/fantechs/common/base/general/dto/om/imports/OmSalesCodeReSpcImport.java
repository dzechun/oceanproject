package com.fantechs.common.base.general.dto.om.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OmSalesCodeReSpcImport implements Serializable {

    /**
     * 销售编码
     */
    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30,orderNum="")
    private String salesCode;

    /**
     * 同包装编码
     */
    @ApiModelProperty(name="samePackageCode",value = "同包装编码")
    @Excel(name = "同包装编码", height = 20, width = 30,orderNum="")
    private String samePackageCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialId",value = "物料编码")
    private Long materialId;

    /**
     * 客户型号ID
     */
    @ApiModelProperty(name="materialModelId",value = "客户型号ID")
    private Long materialModelId;

    /**
     * 优先级
     */
    @ApiModelProperty(name="priority",value = "优先级")
    @Excel(name = "优先级", height = 20, width = 30,orderNum="")
    private Integer priority;

    /**
     * 同包装编码状态(1:激活;2:失效;3：关闭；)
     */
    @ApiModelProperty(name="samePackageCodeStatus",value = "同包装编码状态(1:激活;2:失效;3：关闭；)")
    @Excel(name = "同包装编码状态(1:激活;2:失效;3：关闭；)", height = 20, width = 30,orderNum="")
    private Byte samePackageCodeStatus;

    /**
     * 同包装编码数量
     */
    @ApiModelProperty(name="samePackageCodeQty",value = "同包装编码数量")
    @Excel(name = "同包装编码数量", height = 20, width = 30,orderNum="")
    private BigDecimal samePackageCodeQty;

    /**
     * 已匹配数量
     */
    @ApiModelProperty(name="matchedQty",value = "已匹配数量")
    @Excel(name = "已匹配数量", height = 20, width = 30,orderNum="")
    private BigDecimal matchedQty;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode", value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName", value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="")
    private String materialName;

    /**
     * 客户型号编码
     */
    @ApiModelProperty(name = "productModelCode", value = "客户型号编码")
    @Excel(name = "客户型号编码", height = 20, width = 30,orderNum="")
    private String productModelCode;

    /**
     * 客户型号名称
     */
    @ApiModelProperty(name = "productModelName", value = "客户型号名称")
    @Excel(name = "客户型号名称", height = 20, width = 30,orderNum="")
    private String productModelName;

}
