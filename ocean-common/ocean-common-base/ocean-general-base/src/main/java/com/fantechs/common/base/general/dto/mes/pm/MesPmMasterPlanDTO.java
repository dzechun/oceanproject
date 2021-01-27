package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class MesPmMasterPlanDTO extends MesPmMasterPlan implements Serializable {
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
    /**
     * 工单编号
     */
    @Transient
    @ApiModelProperty(value = "工单编号",example = "工单编号")
    @Excel(name = "生产工单编号",orderNum = "0")
    private String workOrderCode;
    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(value = "物料名称",example = "物料名称")
    @Excel(name = "产品名称",orderNum = "2")
    private String materialName;
    /**
     * 物料编号
     */
    @Transient
    @ApiModelProperty(value = "物料编号",example = "物料编号")
    @Excel(name = "产品编码",orderNum = "1")
    private String materialCode;
    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(value = "产品型号",example = "产品型号")
    @Excel(name = "产品型号",orderNum = "3")
    private String productModelName;
    /**
     * 包装规格名称
     */
    @Transient
    @ApiModelProperty(value = "包装规格名称",example = "包装规格名称")
    @Excel(name = "包装规格",orderNum = "4")
    private String packageSpecificationName;
    /**
     * 颜色
     */
    @Transient
    @ApiModelProperty(value = "颜色",example = "颜色")
    @Excel(name = "颜色",orderNum = "5")
    private String color;
    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(value = "产线名称",example = "产线名称")
    @Excel(name = "产线名称",orderNum = "11")
    private String proName;
    /**
     * 工单状态
     */
    @Transient
    @ApiModelProperty(value = "工单状态",example = "工单状态")
    @Excel(name = "工单状态",orderNum = "6")
    private Byte workOrderStatus;

}