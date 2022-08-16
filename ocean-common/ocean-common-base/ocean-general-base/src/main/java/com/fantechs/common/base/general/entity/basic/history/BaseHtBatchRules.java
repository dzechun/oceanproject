package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 批次规则履历表
 * base_ht_batch_rules
 * @author mr.lei
 * @date 2021-04-25 10:36:37
 */
@Data
@Table(name = "base_ht_batch_rules")
public class BaseHtBatchRules extends ValidGroup implements Serializable {
    /**
     * 批次规则履历ID
     */
    @ApiModelProperty(name="htBatchRulesId",value = "批次规则履历ID")
    @Excel(name = "批次规则履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_batch_rules_id")
    private Long htBatchRulesId;

    /**
     * 批次规则ID
     */
    @ApiModelProperty(name="batchRulesId",value = "批次规则ID")
    @Excel(name = "批次规则ID", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_rules_id")
    private Long batchRulesId;

    /**
     * 批次规则名称
     */
    @ApiModelProperty(name="batchRulesName",value = "批次规则名称")
    @Excel(name = "批次规则名称", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_rules_name")
    private String batchRulesName;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 不混货品
     */
    @ApiModelProperty(name="notMixedWith",value = "不混货品")
    @Excel(name = "不混货品", height = 20, width = 30,orderNum="") 
    @Column(name = "not_mixed_with")
    private Byte notMixedWith;

    /**
     * 跟踪批次号(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterBatch",value = "跟踪批次号(0-否 1-是)")
    @Excel(name = "跟踪批次号(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "tail_after_batch")
    private Byte tailAfterBatch;

    /**
     * 跟踪生产日期(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterDateInProduced",value = "跟踪生产日期(0-否 1-是)")
    @Excel(name = "跟踪生产日期(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "tail_after_date_in_produced")
    private Byte tailAfterDateInProduced;

    /**
     * 跟踪收货单号(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterReceivingCode",value = "跟踪收货单号(0-否 1-是)")
    @Excel(name = "跟踪收货单号(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "tail_after_receiving_code")
    private Byte tailAfterReceivingCode;

    /**
     * 跟踪质检日期(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterQualityDate",value = "跟踪质检日期(0-否 1-是)")
    @Excel(name = "跟踪质检日期(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "tail_after_quality_date")
    private Byte tailAfterQualityDate;

    /**
     * 跟踪销售单(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterSaleCode",value = "跟踪销售单(0-否 1-是)")
    @Excel(name = "跟踪销售单(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "tail_after_sale_code")
    private Byte tailAfterSaleCode;

    /**
     * 跟踪供应商(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterSupplier",value = "跟踪供应商(0-否 1-是)")
    @Excel(name = "跟踪供应商(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "tail_after_supplier")
    private Byte tailAfterSupplier;

    /**
     * 跟踪收货日期(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterReceiveDate",value = "跟踪收货日期(0-否 1-是)")
    @Excel(name = "跟踪收货日期(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "tail_after_receive_date")
    private Byte tailAfterReceiveDate;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Excel(name = "货主ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

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

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;
    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String  warehouseName;

    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="")
    private String materialOwnerName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}