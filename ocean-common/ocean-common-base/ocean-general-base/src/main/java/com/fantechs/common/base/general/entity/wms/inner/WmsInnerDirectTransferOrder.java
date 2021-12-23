package com.fantechs.common.base.general.entity.wms.inner;

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
 * 直接调拨单
 * wms_inner_direct_transfer_order
 * @author 81947
 * @date 2021-12-21 15:40:10
 */
@Data
@Table(name = "wms_inner_direct_transfer_order")
public class WmsInnerDirectTransferOrder extends ValidGroup implements Serializable {
    /**
     * 直接调拨单ID
     */
    @ApiModelProperty(name="directTransferOrderId",value = "直接调拨单ID")
    @Id
    @Column(name = "direct_transfer_order_id")
    private Long directTransferOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    @Column(name = "core_source_sys_order_type_code")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    @Column(name = "source_big_type")
    private Byte sourceBigType;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "单据类型编码")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 作业人员ID
     */
    @ApiModelProperty(name="workerUserId",value = "作业人员ID")
    @Column(name = "worker_user_id")
    private Long workerUserId;

    /**
     * 直接调拨单号
     */
    @ApiModelProperty(name="directTransferOrderCode",value = "直接调拨单号")
    @Excel(name = "直接调拨单号", height = 20, width = 30,orderNum="1")
    @Column(name = "direct_transfer_order_code")
    private String directTransferOrderCode;

    /**
     * 单据状态(1-待作业、2-作业中、3-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待作业、2-作业中、3-完成)")
    @Excel(name = "单据状态(1-待作业、2-作业中、3-完成)", height = 20, width = 30,orderNum="2")
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
    @Excel(name = "备注", height = 20, width = 30,orderNum="5")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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

    private static final long serialVersionUID = 1L;
}