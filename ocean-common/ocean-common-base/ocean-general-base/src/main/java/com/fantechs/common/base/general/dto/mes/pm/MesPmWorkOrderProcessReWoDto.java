package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;


@Data
public class MesPmWorkOrderProcessReWoDto extends MesPmWorkOrderProcessReWo implements Serializable {

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30)
    private String workOrderCode;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 产品料号描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "产品料号描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 工序编码
     */
    @Transient
    @ApiModelProperty(name="processCode",value = "工序编码")
    @Excel(name = "工序编码", height = 20, width = 30,orderNum="")
    private String processCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="")
    private String processName;

    /**
     * 工序描述
     */
    @Transient
    @ApiModelProperty(name="processDesc",value = "工序描述")
    @Excel(name = "工序描述", height = 20, width = 30,orderNum="")
    private String processDesc;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name="sectionName",value = "工段名称")
    @Excel(name = "工段名称", height = 20, width = 30,orderNum="")
    private String sectionName;

    /**
     * 工序类别名称
     */
    @Transient
    @ApiModelProperty(name="processDesc",value = "工序类别名称")
    @Excel(name = "工序类别", height = 20, width = 30,orderNum="")
    private String processCategoryName;

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
