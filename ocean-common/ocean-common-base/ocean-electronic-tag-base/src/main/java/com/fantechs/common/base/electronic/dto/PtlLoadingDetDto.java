package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.PtlLoadingDet;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PtlLoadingDetDto extends PtlLoadingDet implements Serializable {

    /**
     * 上料单号
     */
    @ApiModelProperty(name="loadingCode",value = "上料单号")
    @Excel(name = "上料单号", height = 20, width = 30,orderNum="1")
    @NotBlank(groups = ValidGroup.submit.class, message = "上料单号不能为空")
    @Transient
    private String loadingCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    @NotBlank(groups = ValidGroup.submit.class, message = "物料编码不能为空")
    @NotBlank(message = "物料编码不能为空")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    @Transient
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="4")
    @Transient
    private String materialDesc;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="version" ,value="物料版本")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="5")
    @Transient
    private String version;

    /**
     * 储位id
     */
    @ApiModelProperty(name="storageId",value = "储位id")
    @NotNull(groups = ValidGroup.submit.class, message = "储位id不能为空")
    @Transient
    private Long storageId;

    /**
     * 储位编码
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30,orderNum = "8")
    @Transient
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum = "9")
    @Transient
    private String storageName;

    /**
     * 仓库id
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库名称")
    @Transient
    private Long warehouseId;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum = "10")
    @Transient
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum = "11")
    @Transient
    private String warehouseName;

    /**
     * 仓库区域id
     */
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库编码")
    @Transient
    private Long warehouseAreaId;

    /**
     * 仓库区域编码
     */
    @ApiModelProperty(name="warehouseAreaCode" ,value="仓库区域编码")
    @Excel(name = "仓库区域编码", height = 20, width = 30,orderNum="12")
    @Transient
    private String warehouseAreaCode;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="13")
    @Transient
    private String warehouseAreaName;

    /**
     * 电子标签id
     */
    @ApiModelProperty(name="electronicTagId",value = "电子标签id")
    @Transient
    private Long electronicTagId;

    /**
     * 区域设备Id
     */
    @ApiModelProperty(name="equipmentAreaId",value = "区域设备Id")
    @Transient
    private Long equipmentAreaId;

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
}
