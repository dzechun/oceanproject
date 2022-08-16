package com.fantechs.common.base.general.dto.om.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OmSalesCodeReSpcImport implements Serializable {

    /**
     * 销售编码
     */
    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30)
    private String salesCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode", value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * PO号
     */
    @ApiModelProperty(name="samePackageCode",value = "PO号")
    @Excel(name = "PO号", height = 20, width = 30)
    private String samePackageCode;


    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 客户型号ID
     */
    @ApiModelProperty(name="productModelId",value = "客户型号ID")
    private Long productModelId;

    /**
     * 优先级
     */
    @ApiModelProperty(name="priority",value = "优先级")
    @Excel(name = "优先级", height = 20, width = 30)
    private String priority;

    /**
     * 同包装编码数量
     */
    @ApiModelProperty(name="samePackageCodeQty",value = "PO数量")
    @Excel(name = "PO数量", height = 20, width = 30)
    private String samePackageCodeQty;

    /**
     * 同包装编码状态(1:激活;2:失效;3：关闭；)
     */
    /*@ApiModelProperty(name="samePackageCodeStatus",value = "PO状态(1:激活;2:失效;3：关闭；)")
    @Excel(name = "PO状态(1:激活;2:失效;3：关闭；)", height = 20, width = 30,orderNum="4")
    private String samePackageCodeStatus;

    @ApiModelProperty(name="matchedQty",value = "已匹配数量")
    @Excel(name = "已匹配数量", height = 20, width = 30,orderNum="6")
    private String matchedQty;


    @ApiModelProperty(name = "materialName", value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="8")
    private String materialName;

    @ApiModelProperty(name = "productModelCode", value = "客户型号编码")
    @Excel(name = "客户型号编码", height = 20, width = 30,orderNum="9")
    private String productModelCode;


    @ApiModelProperty(name = "productModelName", value = "客户型号名称")
    @Excel(name = "客户型号名称", height = 20, width = 30,orderNum="10")
    private String productModelName;*/

}
