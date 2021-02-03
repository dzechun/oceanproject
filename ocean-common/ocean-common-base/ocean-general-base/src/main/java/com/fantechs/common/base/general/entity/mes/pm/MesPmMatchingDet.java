package com.fantechs.common.base.general.entity.mes.pm;

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
 * 配套单明细表
 * mes_pm_matching_det
 * @author 53203
 * @date 2021-02-02 15:49:38
 */
@Data
@Table(name = "mes_pm_matching_det")
public class MesPmMatchingDet extends ValidGroup implements Serializable {
    /**
     * 配套信息明细ID
     */
    @ApiModelProperty(name="matchingDetId",value = "配套信息明细ID")
    @Excel(name = "配套信息明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "matching_det_id")
    private Long matchingDetId;

    /**
     * 配套信息ID
     */
    @ApiModelProperty(name="matchingId",value = "配套信息ID")
    @Excel(name = "配套信息ID", height = 20, width = 30,orderNum="") 
    @Column(name = "matching_id")
    private Long matchingId;

    /**
     * 部件工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "部件工单ID")
    @Excel(name = "部件工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 部件质检合格数量
     */
    @ApiModelProperty(name="qualifiedQuantity",value = "部件质检合格数量")
    @Excel(name = "部件质检合格数量", height = 20, width = 30,orderNum="") 
    @Column(name = "qualified_quantity")
    private BigDecimal qualifiedQuantity;

    /**
     * 合格部件使用数量
     */
    @ApiModelProperty(name="usedQuantity",value = "部件用量")
    @Excel(name = "合格部件使用数量", height = 20, width = 30,orderNum="")
    @Column(name = "used_quantity")
    private BigDecimal usedQuantity;

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
    @Column(name = "organization_id")
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

    private static final long serialVersionUID = 1L;
}