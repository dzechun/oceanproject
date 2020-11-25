package com.fantechs.common.base.entity.apply;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 备料信息表
 * smt_stock
 * @author mr.lei
 * @date 2020-11-24 14:52:57
 */
@Data
@Table(name = "smt_stock")
public class SmtStock extends ValidGroup implements Serializable {
    /**
     * 备料id
     */
    @ApiModelProperty(name="stockId",value = "备料id")
    @Excel(name = "备料id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "stock_id")
    private Long stockId;

    /**
     * 备料单号
     */
    @ApiModelProperty(name="stockCode",value = "备料单号")
    @Excel(name = "备料单号", height = 20, width = 30,orderNum="") 
    @Column(name = "stock_code")
    private String stockCode;

    /**
     * 工单id
     */
    @ApiModelProperty(name="workOrderId",value = "工单id")
    @Excel(name = "工单id", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 计划配送时间
     */
    @ApiModelProperty(name="planStockTime",value = "计划配送时间")
    @Excel(name = "计划配送时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_stock_time")
    private Date planStockTime;

    /**
     * 配送方式(0、AGV 1、非AGV)
     */
    @ApiModelProperty(name="deliveryMode",value = "配送方式(0、AGV 1、非AGV)")
    @Excel(name = "配送方式(0、AGV 1、非AGV)", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_mode")
    private Byte deliveryMode;

    /**
     * 单据状态(0、无效 1、有效)
     */
    @ApiModelProperty(name="status",value = "单据状态(0、无效 1、有效)")
    @Excel(name = "单据状态(0、无效 1、有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

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

    private static final long serialVersionUID = 1L;
}