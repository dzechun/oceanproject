package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesPmDailyPlanDetDto extends MesPmDailyPlanDet implements Serializable {

    /**
     * 工单编码
     */
    @Transient
    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    private String workOrderCode;

    /**
     * 订单号
     */
    @Transient
    @ApiModelProperty(name="salesOrderCode",value = "订单号")
    private String salesOrderCode;

    /**
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;


    /**
     * 物料名称.
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    private BigDecimal workOrderQty;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;


    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

}
