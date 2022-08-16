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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 产品关键事项确认单
 * mes_pm_production_key_issues_order
 * @author admin
 * @date 2021-06-11 11:19:19
 */
@Data
@Table(name = "mes_pm_production_key_issues_order")
public class MesPmProductionKeyIssuesOrder extends ValidGroup implements Serializable {
    /**
     * 产品关键事项确认单ID
     */
    @ApiModelProperty(name="productionKeyIssuesOrderId",value = "产品关键事项确认单ID")
    @Id
    @Column(name = "production_key_issues_order_id")
    private Long productionKeyIssuesOrderId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId",value = "产品ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 单据状态(1-待确认 2-已确认)
     */
    @ApiModelProperty(name="order_status",value = "单据状态(1-待确认 2-已确认)")
    private Byte orderStatus;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
    @Transient
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    /**
     * 产品型号编码
     */
    @ApiModelProperty(name="materialCode" ,value="产品型号编码")
    @Transient
    @Excel(name = "产品型号编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName" ,value="产品名称")
    @Transient
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    /**
     * 产品型号描述
     */
    @ApiModelProperty(name="materialDesc" ,value="产品型号描述")
    @Transient
    @Excel(name = "产品型号描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 产品版本
     */
    @ApiModelProperty(name="materialVersion" ,value="产品版本")
    @Transient
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="5")
    private String materialVersion;

    /**
     * 物料属性(0.半成品，1.成品)
     */
    @ApiModelProperty(name="materialProperty" ,value="物料属性(0.半成品，1.成品)")
    @Transient
    @Excel(name = "物料属性(0.半成品，1.成品)", height = 20, width = 30,orderNum="6")
    private Byte materialProperty;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Transient
    @Excel(name = "工单号", height = 20, width = 30,orderNum="1")
    private String workOrderCode;


    /**
     * 产品关键事项确认明细
     */
    @ApiModelProperty(name="mesPmProductionKeyIssuesOrderDetList" ,value="产品关键事项确认明细")
    private List<MesPmProductionKeyIssuesOrderDet> mesPmProductionKeyIssuesOrderDetList = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}