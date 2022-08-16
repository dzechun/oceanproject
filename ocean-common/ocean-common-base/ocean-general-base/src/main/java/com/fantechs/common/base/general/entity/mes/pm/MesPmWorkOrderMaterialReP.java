package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 生产管理-工序物料清单表
 * mes_pm_work_order_material_re_p
 */
@Data
@Table(name = "mes_pm_work_order_material_re_p")
public class MesPmWorkOrderMaterialReP extends ValidGroup implements Serializable {
    /**
     * 工序物料清单表ID
     */
    @ApiModelProperty(name="workOrderMaterialRePId",value = "工序物料清单表ID")
    @Excel(name = "工序物料清单表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "work_order_material_re_p_id")
    private Long workOrderMaterialRePId;

    /**
     * 物料工序关系表ID
     */
    @ApiModelProperty(name="workOrderProcessReWoId",value = "物料工序关系表ID")
    @Excel(name = "物料工序关系表ID", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_process_re_wo_id")
    private Long workOrderProcessReWoId;

    /**
     * 扫描类别(1-物料 2-条码)
     */
    @ApiModelProperty(name="scanType",value = "扫描类别(1-物料 2-条码)")
    @Excel(name = "扫描类别(1-物料 2-条码)", height = 20, width = 30,orderNum="")
    @Column(name = "scan_type")
    private Byte scanType;

    /**
     * 标签类别ID
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别ID")
    @Excel(name = "标签类别ID", height = 20, width = 30,orderNum="")
    @Column(name = "label_category_id")
    private Long labelCategoryId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 单个用量
     */
    @ApiModelProperty(name="usageQty",value = "单个用量")
    @Excel(name = "单个用量", height = 20, width = 30,orderNum="")
    @Column(name = "usage_qty")
    private BigDecimal usageQty;

    /**
     * 零件替代料ID
     */
    @ApiModelProperty(name="subMaterialId",value = "零件替代料ID")
    @Excel(name = "零件替代料ID", height = 20, width = 30,orderNum="")
    @Column(name = "sub_material_id")
    private Long subMaterialId;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}
