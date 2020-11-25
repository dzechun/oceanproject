package com.fantechs.common.base.entity.apply;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 备料详情表
 * smt_stock_det
 * @author mr.lei
 * @date 2020-11-24 14:52:59
 */
@Data
@Table(name = "smt_stock_det")
public class SmtStockDet implements Serializable {
    /**
     * 备料详情表
     */
    @ApiModelProperty(name="stockDetId",value = "备料详情表")
    @Excel(name = "备料详情表", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "stock_det_id")
    private Long stockDetId;

    /**
     * 备料id
     */
    @ApiModelProperty(name="stockId",value = "备料id")
    @Excel(name = "备料id", height = 20, width = 30,orderNum="") 
    @Column(name = "stock_id")
    private Long stockId;

    /**
     * 零件料号Id
     */
    @ApiModelProperty(name="materialId",value = "零件料号Id")
    @Excel(name = "零件料号Id", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQuantity",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_quantity")
    private BigDecimal planQuantity;

    /**
     * 备料数量
     */
    @ApiModelProperty(name="stockQuantity",value = "备料数量")
    @Excel(name = "备料数量", height = 20, width = 30,orderNum="") 
    @Column(name = "stock_quantity")
    private BigDecimal stockQuantity;

    /**
     * 状态(0、无效 1、有效)
     */
    @ApiModelProperty(name="status",value = "状态(0、无效 1、有效)")
    @Excel(name = "状态(0、无效 1、有效)", height = 20, width = 30,orderNum="") 
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