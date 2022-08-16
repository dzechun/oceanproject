package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesPmDailyPlanDto extends MesPmDailyPlan implements Serializable {

    /**
     * 生产数量
     */
    @Transient
    @ApiModelProperty(name="workOrderQty",value = "生产数量")
    private BigDecimal workOrderQty;

    /**
     * 未排产数量
     */
    @Transient
    @ApiModelProperty(name="scheduleQty",value = "未排产数量")
    private BigDecimal noScheduleQty;

    /**
     * 投产数量
     */
    @ApiModelProperty(name="productionQty",value = "投产数量")
    @Transient
    private BigDecimal productionQty;

    /**
     * 物料名称.
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;

    /**
     * 工单编码
     */
    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 工单排产数量
     */
    @ApiModelProperty(name="scheduledQty",value = "排产数量")
    private BigDecimal workOrderScheduledQty;

    /**
     * 工单完成数量
     */
    @ApiModelProperty(name="finishedQty",value = "完成数量")
    private BigDecimal workOrderFinishedQty;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

}
