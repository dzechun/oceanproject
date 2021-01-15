package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;


@Data
public class BasePlatePartsDto extends BasePlateParts implements Serializable {

    /**
     * 产品编码
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 产品名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30)
    private String materialName;

    /**
     * 型号名称
     */
    @Transient
    @ApiModelProperty(name="productModelName",value = "型号名称")
    @Excel(name = "型号名称", height = 20, width = 30)
    private String productModelName;

    /**
     * 颜色
     */
    @Transient
    @ApiModelProperty(name="color",value = "颜色")
    @Excel(name = "颜色", height = 20, width = 30)
    private String color;

    /**
     * 规格编码
     */
    @Transient
    @ApiModelProperty(name="packageSpecificationCode",value = "规格编码")
    @Excel(name = "规格编码", height = 20, width = 30)
    private String packageSpecificationCode;

    /**
     * 规格名称
     */
    @Transient
    @ApiModelProperty(name="packageSpecificationName",value = "规格名称")
    @Excel(name = "规格名称", height = 20, width = 30)
    private String packageSpecificationName;

    /**
     * 材质
     */
    @Transient
    @ApiModelProperty(name="materialQuality",value = "材质")
    @Excel(name = "材质", height = 20, width = 30)
    private String materialQuality;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
