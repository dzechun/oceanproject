package com.fantechs.common.base.general.entity.om;

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
 * 采退订单
 * om_purchase_return_order
 * @author admin
 * @date 2021-12-20 17:04:51
 */
@Data
@Table(name = "om_purchase_return_order")
public class OmPurchaseReturnOrder extends ValidGroup implements Serializable {
    /**
     * 采退订单ID
     */
    @ApiModelProperty(name="purchaseReturnOrderId",value = "采退订单ID")
    @Id
    @Column(name = "purchase_return_order_id")
    private Long purchaseReturnOrderId;

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
    //@Excel(name = "来源系统单据类型编码", height = 20, width = 30,orderNum="")
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
     * 采退订单单号
     */
    @ApiModelProperty(name="purchaseReturnOrderCode",value = "采退订单单号")
    @Excel(name = "采退订单单号", height = 20, width = 30,orderNum="1",needMerge = true)
    @Column(name = "purchase_return_order_code")
    private String purchaseReturnOrderCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 制单人员ID
     */
    @ApiModelProperty(name="makeOrderUserId",value = "制单人员ID")
    @Column(name = "make_order_user_id")
    private Long makeOrderUserId;

    /**
     * 退货部门ID
     */
    @ApiModelProperty(name="returnDeptId",value = "退货部门ID")
    @Column(name = "return_dept_id")
    private Long returnDeptId;

    /**
     * 订单状态(1-打开 2-下发中  3-已下发 4-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-打开 2-下发中  3-已下发 4-完成)")
    //@Excel(name = "订单状态(1-打开 2-下发中  3-已下发 4-完成)", height = 20, width = 30,orderNum="")
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