package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 出库计划单
 * wms_out_plan_delivery_order
 * @author admin
 * @date 2021-12-22 14:55:41
 */
@Data
@Table(name = "wms_out_plan_delivery_order")
public class WmsOutPlanDeliveryOrder extends ValidGroup implements Serializable {
    /**
     * 出库计划ID
     */
    @ApiModelProperty(name="planDeliveryOrderId",value = "出库计划ID")
    @Id
    @Column(name = "plan_delivery_order_id")
    private Long planDeliveryOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    //@Excel(name = "核心系统单据类型编码", height = 20, width = 30,orderNum="")
    @Column(name = "core_source_sys_order_type_code")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    @Excel(name = "来源系统单据类型编码", height = 20, width = 30,orderNum="3",needMerge = true)
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    //@Excel(name = "来源大类(1-系统下推 2-自建 3-第三方系统)", height = 20, width = 30,orderNum="")
    @Column(name = "source_big_type")
    private Byte sourceBigType;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    //@Excel(name = "系统单据类型编码", height = 20, width = 30,orderNum="")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 出库计划单单号
     */
    @ApiModelProperty(name="planDeliveryOrderCode",value = "出库计划单单号")
    @Excel(name = "出库计划单单号", height = 20, width = 30,orderNum="1",needMerge = true)
    @Column(name = "plan_delivery_order_code")
    private String planDeliveryOrderCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 订单状态(1-待执行 2-执行中 3-已执行)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-待执行 2-执行中 3-已执行)")
    @Excel(name = "订单状态(1-待执行 2-执行中 3-已执行)", height = 20, width = 30,orderNum="4",needMerge = true)
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    //@Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="5",needMerge = true)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss",needMerge = true)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss",needMerge = true)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    private static final long serialVersionUID = 1L;
}