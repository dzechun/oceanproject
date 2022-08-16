package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.entity.storage.MesPackageManager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class MesPackageManagerDTO extends MesPackageManager implements Serializable {
    /**
    * 创建用户名称
    */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    @Excel(name = "创建用户名称")
    private String createUserName;
    /**
    * 修改用户名称
    */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称")
    private String modifiedUserName;
    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(value = "组织名称",example = "组织名称")
    @Excel(name = "组织名称")
    private String organizationName;
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(value = "工单号",example = "工单号")
    @Excel(name = "工单号")
    private String workOrderCode;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(value = "物料编码",example = "物料编码")
    @Excel(name = "物料编码")
    private String materialCode;
    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(value = "物料描述",example = "物料描述")
    @Excel(name = "物料描述")
    private String materialDesc;
    /**
     * 物料单位
     */
    @Transient
    @ApiModelProperty(value = "物料单位",example = "物料单位")
    @Excel(name = "物料单位")
    private String unit;
    /**
     * 物料型号
     */
    @Transient
    @ApiModelProperty(value = "物料型号",example = "物料型号")
    @Excel(name = "物料型号")
    private String productModelName;

    /**
     * 储位Id
     */
    @Transient
    @ApiModelProperty(value = "储位Id",example = "储位Id")
    @Excel(name = "储位Id")
    private Long storageId;

    /**
     * 储位名称
     */
    @Transient
    @ApiModelProperty(value = "储位名称",example = "储位名称")
    @Excel(name = "储位名称")
    private String storageName;

    /**
     * 仓库区域Id
     */
    @Transient
    @ApiModelProperty(value = "仓库区域Id",example = "仓库区域Id")
    @Excel(name = "仓库区域Id")
    private Long warehouseAreaId;

    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(value = "仓库区域名称",example = "仓库区域名称")
    @Excel(name = "仓库区域名称")
    private String warehouseAreaName;

    /**
     * 仓库Id
     */
    @Transient
    @ApiModelProperty(value = "仓库Id",example = "仓库Id")
    @Excel(name = "仓库Id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(value = "仓库名称",example = "仓库名称")
    @Excel(name = "仓库名称")
    private String warehouseName;

    /**
     * 合同号
     */
    @Transient
    @ApiModelProperty(value = "合同号",example = "合同号")
    @Excel(name = "合同号")
    private String contractCode;

    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(value = "产线名称",example = "产线名称")
    @Excel(name = "产线名称")
    private String proName;
    /**
     * 包装规格-数量
     */
    @Transient
    @ApiModelProperty(name = "packageSpecificationQuantity",value = "包装规格-数量")
    private BigDecimal packageSpecificationQuantity;

    /**
     * 父栈板码
     */
    @Transient
    @ApiModelProperty(value = "父栈板码",example = "产线名称")
    private String parentBarCode;

    /**
     * 如果扫描的是栈板码，则该字段用于返回该栈板上对应的产品总数
     */
    @Transient
    @ApiModelProperty(value = "栈板上的产品总数",example = "栈板上的产品总数")
    private BigDecimal materialTotal;
}