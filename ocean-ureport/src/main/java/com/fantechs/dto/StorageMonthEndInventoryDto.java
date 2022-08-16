package com.fantechs.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.entity.StorageMonthEndInventory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class StorageMonthEndInventoryDto extends StorageMonthEndInventory implements Serializable {

    /**
     * 客户名称
     */
    @Transient
    @ApiModelProperty(name = "supplierName", value = "客户名称")
    @Excel(name = "客户名称", height = 20, width = 30, orderNum = "1")
    private String supplierName;

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractCode", value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30, orderNum = "2")
    private String contractCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name = "workOrderId", value = "工单ID")
    private Long workOrderId;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName", value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30, orderNum = "3")
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName", value = "仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30, orderNum = "4")
    private String warehouseAreaName;

    /**
     * 储位名称
     */
    @Transient
    @ApiModelProperty(name = "storageName", value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30, orderNum = "5")
    private String storageName;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode", value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30, orderNum = "6")
    private String materialCode;

    /**
     * 产品料号描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc", value = "产品料号描述")
    @Excel(name = "产品料号描述", height = 20, width = 30, orderNum = "7")
    private String materialDesc;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "materialName", value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30, orderNum = "8")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name = "version", value = "版本")
    private String version;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(value = "创建用户名称", example = "创建用户名称")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(value = "修改用户名称", example = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName", value = "组织名称")
    @Transient
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
