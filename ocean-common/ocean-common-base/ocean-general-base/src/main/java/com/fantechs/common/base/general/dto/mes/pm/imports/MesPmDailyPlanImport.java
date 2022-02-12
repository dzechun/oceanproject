package com.fantechs.common.base.general.dto.mes.pm.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author
 * @Date 2021/12/16
 */
@Data
public class MesPmDailyPlanImport implements Serializable {

    /**
     * 产线(必填)
     */
    @Excel(name = "产线(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="proName" ,value="产线(必填)")
    private String proName;

    /**
     * 产线ID
     */
    @Excel(name = "产线ID",  height = 20, width = 30)
    @ApiModelProperty(name="proLineId",value = "产线ID")
    private Long proLineId;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @Excel(name = "工单类型(0、量产 1、试产 2、返工 3、维修)", height = 20, width = 30)
    @ApiModelProperty(name="workOrderTypeName",value = "工单类型(0、量产 1、试产 2、返工 3、维修)")
    private String workOrderTypeName;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @Excel(name = "工单类型(0、量产 1、试产 2、返工 3、维修)", height = 20, width = 30)
    @ApiModelProperty(name="workOrderType",value = "工单类型(0、量产 1、试产 2、返工 3、维修)")
    private Byte workOrderType;


    /**
     * 计划单号(必填)
     */
    @Excel(name = "计划单号(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="dailyPlanCode" ,value="计划单号(必填)")
    private String dailyPlanCode;

    /**
     * 计划开始时间
     */
    @Excel(name = "计划开始时间",  height = 20, width = 30)
    @ApiModelProperty(name="planStartTime" ,value="计划开始时间")
    private Date planStartTime;

    /**
     * 备注
     */
    @Excel(name = "备注",  height = 20, width = 30)
    @ApiModelProperty(name="remark" ,value="备注")
    private String remark;

    /**
     * 工单编号(必填)
     */
    @Excel(name = "工单编号", height = 20, width = 30)
    @ApiModelProperty(name="workOrderCode",value = "工单编号")
    private String workOrderCode;

    /**
     * 工单ID
     */
    @Excel(name = "工单ID", height = 20, width = 30)
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码",  height = 20, width = 30)
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料ID
     */
    @Excel(name = "物料ID",  height = 20, width = 30)
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称",  height = 20, width = 30)
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料规格
     */
    @Excel(name = "物料规格",  height = 20, width = 30)
    @ApiModelProperty(name="materialSpec" ,value="物料规格")
    private String materialSpec;

    /**
     * 包装单位
     */
    @Excel(name = "包装单位",  height = 20, width = 30)
    @ApiModelProperty(name="packingUnitName" ,value="包装单位")
    private String packingUnitName;

    /**
     * 排产数量(必填)
     */
    @Excel(name = "排产数量(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="scheduleQty" ,value="排产数量(必填)")
    private String scheduleQty;
}
