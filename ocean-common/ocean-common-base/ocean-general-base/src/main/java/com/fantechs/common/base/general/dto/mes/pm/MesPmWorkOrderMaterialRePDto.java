package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class MesPmWorkOrderMaterialRePDto extends MesPmWorkOrderMaterialReP implements Serializable {

    /**
     * 标签类别名称
     */
    @Transient
    @ApiModelProperty(value = "标签类别名称",example = "标签类别名称")
    private String labelCategoryName;

    /**
     * 标签类别编码
     */
    @Transient
    @ApiModelProperty(value = "标签类别编码",example = "标签类别编码")
    private String labelCategoryCode;

    /**
     * 零件料号
     */
    @Transient
    @ApiModelProperty(name="materialId",value = "零件料号")
    @Excel(name = "零件料号", height = 20, width = 30,orderNum="")
    private String materialCode;

    /**
     * 零件料号版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion",value = "零件料号版本")
    @Excel(name = "零件料号版本", height = 20, width = 30,orderNum="")
    private String materialVersion;

    /**
     * 零件料号描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "零件料号描述")
    @Excel(name = "零件料号描述", height = 20, width = 30,orderNum="")
    private String materialDesc;

    /**
     * 零件替代料号
     */
    @Transient
    @ApiModelProperty(name="subMaterialCode",value = "零件替代料号")
    @Excel(name = "零件替代料号", height = 20, width = 30,orderNum="")
    private String subMaterialCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    private String modifiedUserName;
    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(value = "组织名称",example = "组织名称")
    private String organizationName;


    private static final long serialVersionUID = 1L;
}
