package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 装车单拣货单明细关系表
 * wms_out_despatch_order_re_jo_re_det
 * @author mr.lei
 * @date 2021-05-10 17:39:14
 */
@Data
@Table(name = "wms_out_despatch_order_re_jo_re_det")
public class WmsOutDespatchOrderReJoReDet extends ValidGroup implements Serializable {
    /**
     * 装车单拣货单明细关系表ID
     */
    @ApiModelProperty(name="despatchOrderReJoReDet",value = "装车单拣货单明细关系表ID")
    @Excel(name = "装车单拣货单明细关系表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "despatch_order_re_jo_re_det")
    private Long despatchOrderReJoReDet;

    /**
     * 装车单拣货单关系表ID
     */
    @ApiModelProperty(name="despatchOrderReJoId",value = "装车单拣货单关系表ID")
    @Excel(name = "装车单拣货单关系表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "despatch_order_re_jo_id")
    private Long despatchOrderReJoId;

    /**
     * 拣货单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "拣货单明细ID")
    @Excel(name = "拣货单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "job_order_det_id")
    private Long jobOrderDetId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
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

    private static final long serialVersionUID = 1L;
}