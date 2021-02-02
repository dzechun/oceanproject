package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/20 11:48
 * @Description: 流程单及工单相关数据
 * @Version: 1.0
 */
@Data
public class ProcessListWorkOrderDTO {
    /**
     * 工单流转卡任务池ID
     */
    @ApiModelProperty(name="workOrderCardPoolId",value = "工单流转卡任务池ID")
    private Long workOrderCardPoolId;
    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    private Long parentId;
    /**
     * 工单流转卡编码
     */
    @ApiModelProperty(name="workOrderCardId",value = "工单流转卡编码")
    private String workOrderCardId;
    /**
     * 部件组成明细Id
     */
    @ApiModelProperty(name="platePartsDetId" ,value="部件组成明细Id")
    private Long platePartsDetId;
    /**
     * 产品Id
     */
    @ApiModelProperty(name="materialId" ,value="产品Id")
    private Long materialId;
    /**
     * 产品编码.
     */
    @ApiModelProperty(name="materialCode" ,value="产品编码")
    private String materialCode;
    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName" ,value="产品名称")
    private String materialName;
    /**
     * 版本
     */
    @ApiModelProperty(name="version" ,value="版本")
    private String version;
    /**
     * 产品描述
     */
    @ApiModelProperty(name="materialDesc" ,value="产品描述")
    private String materialDesc;
    /**
     * 线别名称
     */
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;
    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;
    /**
     * 工艺路线名称
     */
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;
    /**
     * 订单号
     */
    @ApiModelProperty(name="orderCode" ,value="订单号")
    private String orderCode;
    /**
     * 包装单位-名称
     */
    @ApiModelProperty(name = "packingUnitName",value = "包装单位-名称")
    private String packingUnitName;
    /**
     * 包装规格-数量
     */
    @Transient
    @ApiModelProperty(name = "packageSpecificationQuantity",value = "包装规格-数量")
    private String packageSpecificationQuantity;
    /**
     * 产品颜色
     */
    @ApiModelProperty(name = "color",value = "产品颜色")
    private String color;
    /**
     * 产品型号
     */
    @ApiModelProperty(name = "productModuleName",value = "产品型号")
    private String productModuleName;
    /**
     * 已报工总数
     */
    @ApiModelProperty(name = "outputTotalQty",value = "已报工总数")
    private BigDecimal outputTotalQty;
    /**
     * 上工序报工数
     */
    @ApiModelProperty(name = "preQty",value = "上工序报工数")
    private BigDecimal preQty;
    /**
     * 工单ID
     */
    @ApiModelProperty(name = "workOrderId",value = "工单ID")
    private Long workOrderId;
    /**
     * 工单编码
     */
    @ApiModelProperty(name = "workOrderCode",value = "工单编码")
    private String workOrderCode;
    /**
     * 工单总数
     */
    @ApiModelProperty(name = "workOrderQuantity",value = "工单总数")
    private BigDecimal workOrderQuantity;
    /**
     * 投产数量
     */
    @Transient
    @ApiModelProperty(name="productionQuantity" ,value="投产数量")
    private BigDecimal productionQuantity;
    /**
     * 最后修改人名称
     */
    @ApiModelProperty(name = "modifiedUserName",value = "最后修改人名称")
    private String modifiedUserName;
}
