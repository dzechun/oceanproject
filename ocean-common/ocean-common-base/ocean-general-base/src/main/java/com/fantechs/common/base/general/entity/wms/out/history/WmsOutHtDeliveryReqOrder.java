package com.fantechs.common.base.general.entity.wms.out.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 出库通知单履历表
 * wms_out_ht_delivery_req_order
 * @author admin
 * @date 2021-12-21 11:45:46
 */
@Data
@Table(name = "wms_out_ht_delivery_req_order")
public class WmsOutHtDeliveryReqOrder extends ValidGroup implements Serializable {
    /**
     * 出库通知单履历ID
     */
    @ApiModelProperty(name="htDeliveryReqOrderId",value = "出库通知单履历ID")
    @Excel(name = "出库通知单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_delivery_req_order_id")
    private Long htDeliveryReqOrderId;

    /**
     * 出库通知单ID
     */
    @ApiModelProperty(name="deliveryReqOrderId",value = "出库通知单ID")
    @Excel(name = "出库通知单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_req_order_id")
    private Long deliveryReqOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    @Excel(name = "核心系统单据类型编码", height = 20, width = 30,orderNum="") 
    @Column(name = "core_source_sys_order_type_code")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    @Excel(name = "来源系统单据类型编码", height = 20, width = 30,orderNum="") 
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    @Excel(name = "来源大类(1-系统下推 2-自建 3-第三方系统)", height = 20, width = 30,orderNum="") 
    @Column(name = "source_big_type")
    private Byte sourceBigType;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    @Excel(name = "系统单据类型编码", height = 20, width = 30,orderNum="") 
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 出库通知单号
     */
    @ApiModelProperty(name="deliveryReqOrderCode",value = "出库通知单号")
    @Excel(name = "出库通知单号", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_req_order_code")
    private String deliveryReqOrderCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 订单状态(1-待执行 2-执行中 3-已执行)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-待执行 2-执行中 3-已执行)")
    @Excel(name = "订单状态(1-待执行 2-执行中 3-已执行)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
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

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="") 
    private String option3;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="21")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="23")
    private String modifiedUserName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="23")
    private String warehouseName;

    private static final long serialVersionUID = 1L;
}