package com.fantechs.common.base.general.dto.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class EngPackingOrderTakeDto implements Serializable {
    /**
     * 装箱单ID
     */
    @ApiModelProperty(name="packingOrderId",value = "装箱单ID")
    @Id
    private Long packingOrderId;

    /**
     * 装箱单号
     */
    @ApiModelProperty(name="packingOrderCode",value = "装箱单号")
    @Excel(name = "装箱单号", height = 20, width = 30,orderNum="2")
    private String packingOrderCode;

    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    @Excel(name = "发运批次", height = 20, width = 30,orderNum="1")
    private String despatchBatch;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderTime",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="3")
    private Date orderTime;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 总箱数
     */
    @ApiModelProperty(name="totalCartonQty",value = "总箱数")
    private Integer totalCartonQty;

    /**
     * 出厂时间
     */
    @ApiModelProperty(name="leaveFactoryTime",value = "出厂时间")
    @Excel(name = "出厂时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date leaveFactoryTime;

    /**
     * 离港时间
     */
    @ApiModelProperty(name="leavePortTime",value = "离港时间")
    @Excel(name = "离港时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date leavePortTime;

    /**
     * 到港时间
     */
    @ApiModelProperty(name="arrivalPortTime",value = "到港时间")
    @Excel(name = "到港时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date arrivalPortTime;

    /**
     * 到场时间
     */
    @ApiModelProperty(name="arrivalTime",value = "到场时间")
    @Excel(name = "到场时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date arrivalTime;

    /**
     * 订单状态(1-未到达 2-待收货 3-收货中 4-待上架 5-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-未到达 2-待收货 3-收货中 4-待上架 5-完成)")
    @Excel(name = "订单状态(1-未到达 2-待收货 3-收货中 4-待上架 5-完成)", height = 20, width = 30,orderNum="12")
    private Byte orderStatus;

    /**
     * 审核状态(1-未审核 2-审核中 3-已通过 4-未通过)
     */
    @ApiModelProperty(name="auditStatus",value = "审核状态(1-未审核 2-审核中 3-已通过 4-未通过)")
    private Byte auditStatus;

    /**
     * 物流商ID
     */
    @ApiModelProperty(name="shipmentEnterpriseId",value = "物流商ID")
    private Long shipmentEnterpriseId;


    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="13")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="17",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 到达登记人id
     */
    @ApiModelProperty(name = "agoConfirmUserId",value = "到达登记人id")
    private Long agoConfirmUserId;

    /**
     * 供应商编码
     */
    @Transient
    @ApiModelProperty(name = "supplierCode",value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="7")
    private String supplierName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="14")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="16")
    private String modifiedUserName;


    /**
     * 物流商名称
     */
    @Transient
    @ApiModelProperty(name="shipmentEnterpriseName",value = "物流商名称")
    private String shipmentEnterpriseName;

    /**
     * 到货登记人名称
     */
    @Transient
    @ApiModelProperty(name = "agoConfirmUserName",value = "到货登记人名称")
    private String agoConfirmUserName;

    /**
     * 总数量
     */
    @Transient
    @ApiModelProperty(name = "totalQty",value = "总数量")
    @Excel(name = "总数量", height = 20, width = 30,orderNum="4")
    private BigDecimal totalQty;

    /**
     * 分配数量
     */
    @Transient
    @ApiModelProperty(value = "分配数量",name = "totalDistributionQty")
    private BigDecimal totalDistributionQty;

    /**
     * 上架数量
     */
    @Transient
    @ApiModelProperty( value = "上架数量",name = "totalPutawayQty")
    @Excel(name = "上架数量", height = 20, width = 30,orderNum="5")
    private BigDecimal totalPutawayQty;

    /**
     * 收货数量
     */
    @Transient
    @ApiModelProperty(value = "收货数量",name = "totalReceivingQty")
    @Excel(name = "收货数量", height = 20, width = 30,orderNum="6")
    private BigDecimal totalReceivingQty;
}
