package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class SmtSortingDto extends SmtSorting implements Serializable {

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum = "4")
    @Transient
    private String storageName;


    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="8")
    @Transient
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="10")
    @Transient
    private String materialDesc;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum = "11")
    @Transient
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name = "warehouseAreaName",value = "仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum = "13")
    @Transient
    private String warehouseAreaName;


    /**
     * 电子标签id
     */
    @ApiModelProperty(name="electronicTagId",value = "电子标签id")
    @Transient
    private String electronicTagId;

}
