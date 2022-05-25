package com.fantechs.provider.wanbao.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "middle_order")
public class MiddleOrder implements Serializable {

    @Id
    @Column(name = "work_order_id")
    private String workOrderId;

    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    @Column(name = "work_order_code")
    private String workOrderCode;

    @ApiModelProperty(name="workOrderStatus",value = "工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)")
    @Column(name = "work_order_status")
    private String workOrderStatus;

    @ApiModelProperty(name="workOrderType",value = "工单类型(0、量产 1、试产 2、返工 3、维修)")
    @Column(name = "work_order_type")
    private String workOrderType;

    @ApiModelProperty(name="salesOrderCode",value = "销售订单号")
    @Column(name = "sales_order_code")
    private String salesOrderCode;

    @Column(name = "pro_name")
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;

    @Column(name = "pro_code")
    @ApiModelProperty(name="proCode" ,value="线别编码")
    private String proCode;

    @Column
    @ApiModelProperty(name = "option1", value = "计划组")
    private String option1;

    @ApiModelProperty(name="planStartTime",value = "计划开始时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_start_time")
    private String planStartTime;

    @ApiModelProperty(name="planEndTime",value = "计划结束时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_end_time")
    private String planEndTime;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Column(name = "material_code")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Column(name = "material_name")
    private String materialName;

    @ApiModelProperty(name="outputQty",value = "完工数量")
    @Column(name = "output_qty")
    private String outputQty;

    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    @Column(name = "work_order_qty")
    private String workOrderQty;

    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Column(name = "modified_time")
    private Date modifiedTime;

    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Column(name = "sales_code")
    private String salesCode;
}
