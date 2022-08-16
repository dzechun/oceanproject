package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.MesOrderMaterial;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class MesOrderMaterialDTO extends MesOrderMaterial {
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
     * 产品型号
     */
    @Transient
    @ApiModelProperty(value = "产品型号",example = "产品型号")
    @Excel(name = "产品型号")
    private String productModelName;
    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(value = "产品料号",example = "产品料号")
    @Excel(name = "产品料号")
    private String materialCode;
    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(value = "产品版本",example = "产品版本")
    @Excel(name = "产品版本")
    private String version;
    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(value = "产品描述",example = "产品描述")
    @Excel(name = "产品描述")
    private String materialDesc;

    /**
     * 包装方式
     */
    @ApiModelProperty(value = "包装方式",example = "包装方式")
    @Column(name = "packing_unit_name")
    @Excel(name = "包装方式")
    private String packingUnitName;

}