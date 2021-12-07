package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 单据流配置
 * base_order_flow
 * @author Dylan
 * @date 2021-12-07 11:43:53
 */
@Data
@Table(name = "base_order_flow")
public class BaseOrderFlow extends ValidGroup implements Serializable {
    /**
     * 单据流配置ID
     */
    @ApiModelProperty(name="orderFlowId",value = "单据流配置ID")
    @Excel(name = "单据流配置ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "order_flow_id")
    private Long orderFlowId;

    /**
     * 作业模块(1-入库作业 2-库内作业 3-出库作业)
     */
    @ApiModelProperty(name="operationModule",value = "作业模块(1-入库作业 2-库内作业 3-出库作业)")
    @Excel(name = "作业模块(1-入库作业 2-库内作业 3-出库作业)", height = 20, width = 30,orderNum="") 
    @Column(name = "operation_module")
    private Byte operationModule;

    /**
     * 业务类型
     */
    @ApiModelProperty(name="businessType",value = "业务类型")
    @Excel(name = "业务类型", height = 20, width = 30,orderNum="") 
    @Column(name = "business_type")
    private Byte businessType;

    /**
     * 单据流维度(1-通用 2-供应商 3-物料)
     */
    @ApiModelProperty(name="orderFlowDimension",value = "单据流维度(1-通用 2-供应商 3-物料)")
    @Excel(name = "单据流维度(1-通用 2-供应商 3-物料)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_flow_dimension")
    private Byte orderFlowDimension;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 单据节点(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)
     */
    @ApiModelProperty(name="orderNode",value = "单据节点(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)")
    @Excel(name = "单据节点(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_node")
    private Byte orderNode;

    /**
     * 来源单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)
     */
    @ApiModelProperty(name="sourceOrder",value = "来源单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)")
    @Excel(name = "来源单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)", height = 20, width = 30,orderNum="") 
    @Column(name = "source_order")
    private Byte sourceOrder;

    /**
     * 下推单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)
     */
    @ApiModelProperty(name="pushDownOrder",value = "下推单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)")
    @Excel(name = "下推单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)", height = 20, width = 30,orderNum="") 
    @Column(name = "push_down_order")
    private Byte pushDownOrder;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    @Column(name = "remark")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}