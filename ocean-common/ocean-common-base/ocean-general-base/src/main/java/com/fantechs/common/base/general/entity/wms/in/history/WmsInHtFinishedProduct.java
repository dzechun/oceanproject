package com.fantechs.common.base.general.entity.wms.in.history;

import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 成品入库单
 * wms_in_ht_finished_product
 * @author hyc
 * @date 2021-01-07 16:16:15
 */
@Data
@Table(name = "wms_in_ht_finished_product")
public class WmsInHtFinishedProduct extends ValidGroup implements Serializable {
    /**
     * 履历ID
     */
    @ApiModelProperty(name="htFinishedProductId",value = "履历ID")
    @Id
    @Column(name = "ht_finished_product_id")
    @NotNull(groups = update.class,message = "履历ID")
    private Long htFinishedProductId;

    /**
     * 成品入库单ID
     */
    @ApiModelProperty(name="finishedProductId",value = "成品入库单ID")
    @Column(name = "finished_product_id")
    private Long finishedProductId;

    /**
     * 成品入库单号
     */
    @ApiModelProperty(name="finishedProductCode",value = "成品入库单号")
    @Column(name = "finished_product_code")
    private String finishedProductCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 处理人
     */
    @ApiModelProperty(name="operatorUserId",value = "处理人")
    @Column(name = "operator_user_id")
    private Long operatorUserId;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="inTime",value = "单据日期")
    @Column(name = "in_time")
    private Date inTime;

    /**
     * 单据类型（0-成品入库）
     */
    @ApiModelProperty(name="inType",value = "单据类型（0-成品入库）")
    @Column(name = "in_type")
    private Byte inType;

    /**
     * 单据状态（0-待入库 1-入库中 2-入库完成）
     */
    @ApiModelProperty(name="inStatus",value = "单据状态（0-待入库 1-入库中 2-入库完成）")
    @Column(name = "in_status")
    private Byte inStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

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
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="operatorUserName" ,value="处理人")
    private String operatorUserName;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode" ,value="组织编码")
    private String organizationCode;

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

    private static final long serialVersionUID = 1L;
}