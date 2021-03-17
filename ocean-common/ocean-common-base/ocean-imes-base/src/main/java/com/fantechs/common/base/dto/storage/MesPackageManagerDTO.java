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
     * 储位名称
     */
    @Transient
    @ApiModelProperty(value = "储位名称",example = "储位名称")
    @Excel(name = "储位名称")
    private String storageName;
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
}