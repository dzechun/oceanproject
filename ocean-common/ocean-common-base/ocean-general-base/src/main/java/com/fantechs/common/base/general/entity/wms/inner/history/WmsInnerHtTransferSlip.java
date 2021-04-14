package com.fantechs.common.base.general.entity.wms.inner.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 调拨单履历信息表
 * wms_inner_ht_transfer_slip
 * @author 53203
 * @date 2021-03-09 15:59:51
 */
@Data
@Table(name = "wms_inner_ht_transfer_slip")
public class WmsInnerHtTransferSlip extends ValidGroup implements Serializable {
    /**
     * 调拨单履历ID
     */
    @ApiModelProperty(name="htTransferSlipId",value = "调拨单履历ID")
    @Excel(name = "调拨单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_transfer_slip_id")
    private Long htTransferSlipId;

    /**
     * 调拨单ID
     */
    @ApiModelProperty(name="transferSlipId",value = "调拨单ID")
    @Excel(name = "调拨单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_slip_id")
    private Long transferSlipId;

    /**
     * 调拨单号
     */
    @ApiModelProperty(name="transferSlipCode",value = "调拨单号")
    @Excel(name = "调拨单号", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_slip_code")
    private String transferSlipCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserId",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="") 
    @Column(name = "processor_user_id")
    private Long processorUserId;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="transferSlipTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_slip_time")
    private Date transferSlipTime;

    /**
     * 单据状态（0-待调拨 1-调拨中 2-调拨完成）
     */
    @ApiModelProperty(name="transferSlipStatus",value = "单据状态（0-待调拨 1-调拨中 2-调拨完成）")
    @Excel(name = "单据状态（0-待调拨 1-调拨中 2-调拨完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_slip_status")
    private Byte transferSlipStatus;

    /**
     * 单据类型（0、库内调拨 1、库外调拨）
     */
    @ApiModelProperty(name="orderType",value = "单据类型（0、库内调拨 1、库外调拨）")
    @Excel(name = "单据类型（0、库内调拨 1、库外调拨）", height = 20, width = 30,replace = {"库内调拨_0","库外调拨_1"})
    @Column(name = "order_type")
    @NotNull(message = "单据类型不能为空")
    private Byte orderType;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long organizationId;

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
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="6")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 处理人
     */
    @Transient
    @ApiModelProperty(name = "processorUserName",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="11")
    private String processorUserName;

    /**
     * 组织编码
     */
    @Transient
    @ApiModelProperty(name = "organizationCode",value = "组织编码")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="12")
    private String organizationCode;

    private static final long serialVersionUID = 1L;
}