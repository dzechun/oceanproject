package com.fantechs.common.base.dto.apply;

import com.fantechs.common.base.entity.apply.SmtWorkOrderReport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2020/11/21
 */
@Data
public class SmtWorkOrderReportDto extends SmtWorkOrderReport implements Serializable {
    private static final long serialVersionUID = -1303301538820730859L;
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @Transient
    @ApiModelProperty(name="workOrderType" ,value="工单类型")
    private Integer workOrderType;

    /**
     * 物料ID
     */
    @Transient
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料料号")
    private String materialCode;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name="workOrderQuantity" ,value="工单数量")
    private Integer workOrderQuantity;

    /**
     * 产出工序
     */
    @Transient
    @ApiModelProperty(name="productionProcess" ,value="产出工序")
    private String productionProcess;

    /**
     * 计划结束时间
     */
    @Transient
    @ApiModelProperty(name="plannedEndTime" ,value="计划结束时间")
    private Date plannedEndTime;

    @Transient
    @ApiModelProperty(name = "completedQuantity",value = "w完工数量")
    private Integer completedQuantity;

    @Transient
    @ApiModelProperty(name = "outPutQuantity",value = "产出数量")
    private Integer outPutQuantity;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
