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
    @Column(name = "work_order_card_pool_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY,generator = "JDBC")
    private Long workOrderCardPoolId;

    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    @Column(name = "parent_id")
    private Long parentId;
    /**
     * 工单流转卡编码
     */
    @ApiModelProperty(name="workOrderCardId",value = "工单流转卡编码")
    @Excel(name = "工单流转卡编码", height = 20, width = 30,orderNum="3")
    @Column(name = "work_order_card_id")
    private String workOrderCardId;
    /**
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30,orderNum="3")
    private String version;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "生产线", height = 20, width = 30,orderNum = "9")
    private String proName;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="10")
    private String routeName;

    /**
     * 订单号
     */
    @Transient
    @ApiModelProperty(name="orderCode" ,value="订单号")
    private String orderCode;

    /**
     * 包装单位-名称
     */
    @Transient
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
    @Transient
    @ApiModelProperty(name = "color",value = "产品颜色")
    private String color;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModuleName",value = "产品型号")
    private String productModuleName;

    /**
     * 已报工总数
     */
    @Transient
    @ApiModelProperty(name = "outputTotalQty",value = "已报工总数")
    private BigDecimal outputTotalQty;
    /**
     * 上工序报工数
     */
    @Transient
    @ApiModelProperty(name = "preQty",value = "上工序报工数")
    private BigDecimal preQty;
    /**
     * 工单编码
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "工单编码")
    private String workOrderCode;
    /**
     * 工单总数
     */
    @Transient
    @ApiModelProperty(name = "workOrderQuantity",value = "工单总数")
    private BigDecimal workOrderQuantity;
    /**
     * 投产数量
     */
    @Transient
    @ApiModelProperty(name="productionQuantity" ,value="投产数量")
    private Integer productionQuantity;
}
