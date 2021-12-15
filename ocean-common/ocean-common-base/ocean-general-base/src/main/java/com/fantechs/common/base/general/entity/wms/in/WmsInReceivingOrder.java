package com.fantechs.common.base.general.entity.wms.in;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;
;

/**
 * 收货单
 * wms_in_receiving_order
 * @author mr.lei
 * @date 2021-12-13 20:06:09
 */
@Data
@Table(name = "wms_in_receiving_order")
public class WmsInReceivingOrder extends ValidGroup implements Serializable {
    /**
     * 收货单ID
     */
    @ApiModelProperty(name="receivingOrderId",value = "收货单ID")
    @Id
    @Column(name = "receiving_order_id")
    private Long receivingOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    @Excel(name = "核心系统单据类型编码", height = 20, width = 30,orderNum="1")
    @Column(name = "core_source_sys_order_type_code")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    @Excel(name = "来源系统单据类型编码", height = 20, width = 30,orderNum="2")
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    @Excel(name = "系统单据类型编码", height = 20, width = 30,orderNum="3")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 收货单号
     */
    @ApiModelProperty(name="receivingOrderCode",value = "收货单号")
    @Excel(name = "收货单号", height = 20, width = 30,orderNum="4")
    @Column(name = "receiving_order_code")
    private String receivingOrderCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 单据状态(1-待执行 2-执行中 3-收货完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待执行 2-执行中 3-收货完成)")
    @Excel(name = "单据状态(1-待执行 2-执行中 3-收货完成)", height = 20, width = 30,orderNum="6",replace = {"1_待执行","2_执行中","3_收货完成"})
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="7")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    @Transient
    @ApiModelProperty(name = "isPdaCreate",value = "是否PDA创建")
    private Byte isPdaCreate;

    @Transient
    private List<WmsInReceivingOrderDetDto> wmsInReceivingOrderDets;

    private static final long serialVersionUID = 1L;
}