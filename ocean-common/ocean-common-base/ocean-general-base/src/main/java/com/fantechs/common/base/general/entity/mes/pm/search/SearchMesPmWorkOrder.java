package com.fantechs.common.base.general.entity.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesPmWorkOrder extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -2565635736485077385L;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;
    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;
    /**
     * 父工单ID
     */
    @ApiModelProperty(name="parentId" ,value="父工单ID")
    private Long parentId;
    /**
     * 工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)
     */
    @ApiModelProperty(name="workOrderStatus" ,value="工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)")
    private String workOrderStatus;

    /**
     * 工单状态集合
     */
    @ApiModelProperty(name="workOrderStatusList" ,value="工单状态集合")
    private List<String> workOrderStatusList;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    private Long materialId;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="processId" ,value="产线ID")
    private Long proLineId;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proName" ,value="产线名称")
    private String proName;

    /**
     * option1
     */
    @ApiModelProperty(name="option1" ,value="扩展字段1")
    private String option1;

    /**
     * option2
     */
    @ApiModelProperty(name="option2" ,value="扩展字段2")
    private String option2;

    /**
     * option3
     */
    @ApiModelProperty(name="option3" ,value="扩展字段3")
    private String option3;

    @ApiModelProperty(name="salesOrderCode" ,value="订单号")
    private String salesOrderCode;

    @ApiModelProperty(name="routeName" ,value="工艺路线")
    private String routeName;

    /**
     * 是否打印（0、打印 1、不打印）
     */
    @ApiModelProperty(name="ifPrint",value = "是否打印（0、打印 1、不打印）")
    private Byte ifPrint;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    @ApiModelProperty(name="logicName" ,value="逻辑仓")
    private String logicName;

    @ApiModelProperty(name="salesCode" ,value="销售编码")
    private String salesCode;
}
