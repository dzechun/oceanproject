package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class BaseStorageCapacityImport implements Serializable {

    /**
     * 物料编码前缀
     */
    @Excel(name = "物料编码前缀(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="materialCodePrefix" ,value="物料编码前缀")
    private String materialCodePrefix;

    /**
     * 物料编码
     */
  /*  @Excel(name = "物料编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;*/

    /**
     * 物料id
     */
   /* @ApiModelProperty(name="materialId" ,value="物料id")
    private Long materialId;*/

    /**
     * A类容量
     */
    @ApiModelProperty(name="typeACapacity",value = "A类容量")
    @Excel(name = "A类容量", height = 20, width = 30)
    private BigDecimal typeACapacity;

    /**
     * B类容量
     */
    @ApiModelProperty(name="typeBCapacity",value = "B类容量")
    @Excel(name = "B类容量", height = 20, width = 30)
    private BigDecimal typeBCapacity;

    /**
     * C类容量
     */
    @ApiModelProperty(name="typeCCapacity",value = "C类容量")
    @Excel(name = "C类容量", height = 20, width = 30)
    private BigDecimal typeCCapacity;

    /**
     * D类容量
     */
    @ApiModelProperty(name="typeDCapacity",value = "D类容量")
    @Excel(name = "D类容量", height = 20, width = 30)
    private BigDecimal typeDCapacity;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

}
