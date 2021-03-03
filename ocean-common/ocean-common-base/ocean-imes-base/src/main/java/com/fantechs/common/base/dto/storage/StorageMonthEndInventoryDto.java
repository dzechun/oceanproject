package com.fantechs.common.base.dto.storage;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.storage.StorageMonthEndInventory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class StorageMonthEndInventoryDto extends StorageMonthEndInventory implements Serializable {

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="1")
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName",value = "仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="2")
    private String warehouseAreaName;

    /**
     * 储位名称
     */
    @Transient
    @ApiModelProperty(name="storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum="3")
    private String storageName;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="4")
    private String materialCode;

    /**
     * 产品料号编码
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "产品料号编码")
    @Excel(name = "产品料号编码", height = 20, width = 30,orderNum="5")
    private String materialDesc;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name="productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="6")
    private String productModelName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
