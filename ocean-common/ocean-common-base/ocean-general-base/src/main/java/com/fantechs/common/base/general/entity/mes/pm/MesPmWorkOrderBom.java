package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 工单BOM
 * mes_pm_work_order_bom
 * @author 81947
 * @date 2021-05-31 09:18:53
 */
@Data
@Table(name = "mes_pm_work_order_bom")
public class MesPmWorkOrderBom extends ValidGroup implements Serializable {
    /**
     * 工单BOM ID
     */
    @ApiModelProperty(name="workOrderBomId",value = "工单BOM ID")
    @Excel(name = "工单BOM ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "work_order_bom_id")
    private Long workOrderBomId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 零件物料ID
     */
    @ApiModelProperty(name="partMaterialId",value = "零件物料ID")
    @Excel(name = "零件物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "part_material_id")
    private Long partMaterialId;

    /**
     * 代用物料ID
     */
    @ApiModelProperty(name="subMaterialId",value = "代用物料ID")
    @Excel(name = "代用物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "sub_material_id")
    private Long subMaterialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_id")
    private Long processId;

    /**
     * 单个用量
     */
    @ApiModelProperty(name="singleQty",value = "单个用量")
    @Excel(name = "单个用量", height = 20, width = 30,orderNum="") 
    @Column(name = "single_qty")
    private BigDecimal singleQty;

    /**
     * 使用数量
     */
    @ApiModelProperty(name="usageQty",value = "使用数量")
    @Excel(name = "使用数量", height = 20, width = 30,orderNum="") 
    @Column(name = "usage_qty")
    private BigDecimal usageQty;

    /**
     * 基准数量
     */
    @ApiModelProperty(name="baseQty",value = "基准数量")
    @Excel(name = "基准数量", height = 20, width = 30,orderNum="") 
    @Column(name = "base_qty")
    private BigDecimal baseQty;

    /**
     * 已发量
     */
    @ApiModelProperty(name="deliveredQty",value = "已发量")
    @Excel(name = "已发量", height = 20, width = 30,orderNum="") 
    @Column(name = "delivered_qty")
    private BigDecimal deliveredQty;

    /**
     * 未发量
     */
    @ApiModelProperty(name="undeliveredQty",value = "未发量")
    @Excel(name = "未发量", height = 20, width = 30,orderNum="") 
    @Column(name = "undelivered_qty")
    private BigDecimal undeliveredQty;

    private String position;

    /**
     * 累计下发数量
     */
    @ApiModelProperty(name="totalIssueQty",value = "累计下发数量")
    //@Excel(name = "累计下发数量", height = 20, width = 30)
    @Column(name = "total_issue_qty")
    private BigDecimal totalIssueQty;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    //@Excel(name = "是否已全部下发(0-否 1-是)", height = 20, width = 30)
    @Column(name = "if_all_issued")
    private Byte ifAllIssued;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}