package com.fantechs.common.base.entity.apply.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "smt_ht_work_order_bom")
@Data
public class SmtHtWorkOrderBom implements Serializable {
    /**
     * 工单BOM历史ID
     */
    @Id
    @Column(name = "ht_work_order_bom_id")
    private Long htWorkOrderBomId;

    /**
     * 工单BOM ID
     */
    @Id
    @Column(name = "work_order_bom_id")
    @ApiModelProperty(name="workOrderBomId" ,value="工单BOM ID")
    private Long workOrderBomId;

    /**
     * 工单ID
     */
    @Column(name = "work_order_id")
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;

    /**
     * 零件物料ID
     */
    @Column(name = "part_material_id")
    @ApiModelProperty(name="partMaterialId" ,value="工单ID")
    private Long partMaterialId;

    /**
     * 代用物料ID
     */
    @Column(name = "sub_material_id")
    @ApiModelProperty(name="subMaterialId" ,value="代用物料ID")
    private Long subMaterialId;

    /**
     * 工序ID
     */
    @Column(name = "process_id")
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

    /**
     * 使用数量
     */
    @ApiModelProperty(name="quantity" ,value="使用数量")
    private BigDecimal quantity;

    /**
     * 位置
     */
    @ApiModelProperty(name="position" ,value="位置")
    private String position;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;
}