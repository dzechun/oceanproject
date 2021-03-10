package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDetDto;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.rmi.MarshalException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 调拨单
 * wms_inner_transfer_slip
 * @author 53203
 * @date 2021-03-05 10:50:49
 */
@Data
@Table(name = "wms_inner_transfer_slip")
public class WmsInnerTransferSlip extends ValidGroup implements Serializable {
    /**
     * 调拨单ID
     */
    @ApiModelProperty(name="transferSlipId",value = "调拨单ID")
    @Id
    @Column(name = "transfer_slip_id")
    @NotNull(groups = update.class,message = "调拨单ID不能为空")
    private Long transferSlipId;

    /**
     * 调拨单号
     */
    @ApiModelProperty(name="transferSlipCode",value = "调拨单号")
    @Excel(name = "调拨单号", height = 20, width = 30,orderNum="2")
    @Column(name = "transfer_slip_code")
    private String transferSlipCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserId",value = "处理人")
    @Column(name = "processor_user_id")
    private Long processorUserId;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="transferSlipTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="3",exportFormat = "yyyy-MM-dd")
    @Column(name = "transfer_slip_time")
    private Date transferSlipTime;

    /**
     * 单据状态（0-待调拨 1-调拨中 2-调拨完成）
     */
    @ApiModelProperty(name="transferSlipStatus",value = "单据状态（0-待调拨 1-调拨中 2-调拨完成）")
    @Excel(name = "单据状态（0-待调拨 1-调拨中 2-调拨完成）", height = 20, width = 30,orderNum="6",replace = {"待调拨_0","调拨中_1","调拨完成_2"})
    @Column(name = "transfer_slip_status")
    private Byte transferSlipStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="7",replace = {"无效_0","有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="8")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
     * 调拨单明细集合
     */
    @ApiModelProperty(name="wmsInnerTransferSlipDets",value = "调拨单明细集合")
    private List<WmsInnerTransferSlipDetDto> wmsInnerTransferSlipDetDtos = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}