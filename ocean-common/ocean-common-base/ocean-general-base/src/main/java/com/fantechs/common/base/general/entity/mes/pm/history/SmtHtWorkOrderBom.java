package com.fantechs.common.base.general.entity.mes.pm.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "base_ht_work_order_bom")
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
     * 单个用量
     */
    @ApiModelProperty(name="singleQuantity" ,value="单个用量")
    private BigDecimal singleQuantity;

    /**
     * 使用数量
     */
    @ApiModelProperty(name="quantity" ,value="使用数量")
    private BigDecimal quantity;

    /**
     * 基准数量
     */
    @ApiModelProperty(name="baseQuantity" ,value="基准数量")
    private BigDecimal baseQuantity;

    /**
     * 位置
     */
    @ApiModelProperty(name="position" ,value="位置")
    private String position;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;


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


    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="partMaterialCode" ,value="零件料号")
    private String partMaterialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 零件替代料号
     */
    @Transient
    @ApiModelProperty(name="subMaterialCode" ,value="零件替代料号")
    private String subMaterialCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;
}
