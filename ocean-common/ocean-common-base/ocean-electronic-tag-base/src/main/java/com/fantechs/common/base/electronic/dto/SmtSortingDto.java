package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SmtSortingDto extends SmtSorting implements Serializable {

    /**
     * 储位编码
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30,orderNum="11")
    @Transient
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum = "12")
    @Transient
    private String storageName;

    /**
     * 物料id
     */
    @ApiModelProperty(name = "materialId",value = "物料Id")
    @Transient
    private String materialId;


    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="6")
    @Transient
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="7")
    @Transient
    private String materialDesc;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="version" ,value="物料版本")
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="5")
    @Transient
    private String version;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum = "10")
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
     * 创建账号
     */
    @Transient
    @ApiModelProperty(name = "createUserCode",value = "创建账号")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="14")
    private String createUserCode;

    /**
     * 修改账号
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserCode",value = "修改账号")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="15")
    private String modifiedUserCode;


    /**
     * 电子标签id
     */
    @ApiModelProperty(name="electronicTagId",value = "电子标签id")
    @Transient
    private String electronicTagId;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 区域设备Id
     */
    @ApiModelProperty(name="equipmentAreaId",value = "区域设备Id")
    @Transient
    private String equipmentAreaId;

}
