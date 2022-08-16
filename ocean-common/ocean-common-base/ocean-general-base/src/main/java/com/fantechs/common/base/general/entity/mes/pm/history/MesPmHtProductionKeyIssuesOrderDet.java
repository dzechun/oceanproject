package com.fantechs.common.base.general.entity.mes.pm.history;

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
 * 产品关键事项确认明细履历表
 * mes_pm_ht_production_key_issues_order_det
 * @author admin
 * @date 2021-06-11 11:19:19
 */
@Data
@Table(name = "mes_pm_ht_production_key_issues_order_det")
public class MesPmHtProductionKeyIssuesOrderDet extends ValidGroup implements Serializable {
    /**
     * 产品关键事项确认单明细履历ID
     */
    @ApiModelProperty(name="htProductionKeyIssuesOrderDetId",value = "产品关键事项确认单明细履历ID")
    @Excel(name = "产品关键事项确认单明细履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_production_key_issues_order_det_id")
    private Long htProductionKeyIssuesOrderDetId;

    /**
     * 产品关键事项确认单明细ID
     */
    @ApiModelProperty(name="productionKeyIssuesOrderDetId",value = "产品关键事项确认单明细ID")
    @Excel(name = "产品关键事项确认单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "production_key_issues_order_det_id")
    private Long productionKeyIssuesOrderDetId;

    /**
     * 产品关键事项确认单ID
     */
    @ApiModelProperty(name="productionKeyIssuesOrderId",value = "产品关键事项确认单ID")
    @Excel(name = "产品关键事项确认单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "production_key_issues_order_id")
    private Long productionKeyIssuesOrderId;

    /**
     * 产品关键事项明细ID
     */
    @ApiModelProperty(name="productionKeyIssuesDetId",value = "产品关键事项明细ID")
    @Excel(name = "产品关键事项明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "production_key_issues_det_id")
    private Long productionKeyIssuesDetId;

    /**
     * 是否
     */
    @ApiModelProperty(name="yesOrNo",value = "是否")
    @Excel(name = "是否", height = 20, width = 30,orderNum="") 
    @Column(name = "yes_or_no")
    private Byte yesOrNo;

    /**
     * 值
     */
    @ApiModelProperty(name="value",value = "值")
    @Excel(name = "值", height = 20, width = 30,orderNum="") 
    private BigDecimal value;

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