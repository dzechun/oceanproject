package com.fantechs.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class WorkOrderUreportDto implements Serializable {

    @ApiModelProperty(name="type",value = "计划类型")
    @Excel(name = "计划类型", height = 20, width = 30)
    private String type;

    @ApiModelProperty(name="proName",value = "产线")
    @Excel(name = "产线", height = 20, width = 30)
    private String proName;

    @ApiModelProperty(name="workOrderCode",value = "计划名字")
    @Excel(name = "", height = 20, width = 30)
    private String workOrderCode;

    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(name = "materialCode",value = "万宝型号")
    @Excel(name = "万宝型号", height = 20, width = 30)
    private String materialCode;

    @ApiModelProperty(name="productModelCode",value = "客户型号")
    @Excel(name = "客户型号", height = 20, width = 30)
    private String productModelCode;

    @ApiModelProperty(name="planStartTime",value = "计划开始时间")
    @Excel(name = "计划开始时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    @ApiModelProperty(name="productionQty",value = "实际生产数量")
    @Excel(name = "实际生产数量", height = 20, width = 30)
    private BigDecimal productionQty;

    @ApiModelProperty(name="actualStartTime",value = "实际开始时间")
    @Excel(name = "实际开始时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date actualStartTime;

    @ApiModelProperty(name="scheduledQty",value = "计划生产数量")
    @Excel(name = "计划生产数量", height = 20, width = 30)
    private BigDecimal scheduledQty;

    @ApiModelProperty(name="actualEndTime",value = "实际完成时间")
    @Excel(name = "实际完成时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date actualEndTime;

    /**
     * 工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)
     */
    @ApiModelProperty(name="work_order_status" ,value="计划状态")
    @Excel(name = "计划状态", height = 20, width = 30, replace = {"下载或手动创建_1","条码打印完成_2","生产中_3","异常挂起_4","取消_5","完工_6","删除_7"})
    private Byte workOrderStatus;

    @ApiModelProperty(name="barcodeList" ,value="条码集合")
    private List<WorkOrderBarcodeDto> barcodeList;
}
