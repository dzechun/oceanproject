package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 备料信息表
 * base_stock
 * @author mr.lei
 * @date 2020-11-24 14:52:57
 */
@Data
@Table(name = "base_stock")
public class SmtStock extends ValidGroup implements Serializable {
    /**
     * 备料id
     */
    @ApiModelProperty(name="stockId",value = "备料id")
    @Id
    @Column(name = "stock_id")
    @NotNull(groups = ValidGroup.update.class,message = "备料Id不能为空")
    private Long stockId;

    /**
     * 备料单号
     */
    @ApiModelProperty(name="stockCode",value = "备料单号")
    @Excel(name = "备料单号", height = 20, width = 30,orderNum="1")
    @Column(name = "stock_code")
    private String stockCode;

    /**
     * 工单id
     */
    @ApiModelProperty(name="workOrderId",value = "工单id")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 计划配送时间
     */
    @ApiModelProperty(name="planStockTime",value = "计划配送时间")
    @Excel(name = "计划配送时间", height = 20, width = 30,orderNum="2",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_stock_time")
    private Date planStockTime;

    /**
     * 配送方式(0、AGV 1、非AGV)
     */
    @ApiModelProperty(name="deliveryMode",value = "配送方式(0、AGV 1、非AGV)")
    @Excel(name = "配送方式(0、AGV 1、非AGV)", height = 20, width = 30,orderNum="3")
    @Column(name = "delivery_mode")
    private Byte deliveryMode;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 单据状态(0、无效 1、有效)
     */
    @ApiModelProperty(name="status",value = "单据状态(0、无效 1、有效)")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
