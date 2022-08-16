package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 物流商信息维护履历表
 * base_ht_shipment_enterprise
 * @author 53203
 * @date 2020-12-16 10:30:58
 */
@Data
@Table(name = "base_ht_shipment_enterprise")
public class BaseHtShipmentEnterprise implements Serializable {
    /**
     * 物流商履历ID
     */
    @ApiModelProperty(name="htShipmentEnterpriseId",value = "物流商履历ID")
    @Excel(name = "物流商履历ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_shipment_enterprise_id")
    private Long htShipmentEnterpriseId;

    /**
     * 物流商ID
     */
    @ApiModelProperty(name="shipmentEnterpriseId",value = "物流商ID")
    @Excel(name = "物流商ID", height = 20, width = 30)
    @Column(name = "shipment_enterprise_id")
    private Long shipmentEnterpriseId;

    /**
     * 物流商编码
     */
    @ApiModelProperty(name="shipmentEnterpriseCode",value = "物流商编码")
    @Excel(name = "物流商编码", height = 20, width = 30)
    @Column(name = "shipment_enterprise_code")
    private String shipmentEnterpriseCode;

    /**
     * 物流商名称
     */
    @ApiModelProperty(name="shipmentEnterpriseName",value = "物流商名称")
    @Excel(name = "物流商名称", height = 20, width = 30)
    @Column(name = "shipment_enterprise_name")
    private String shipmentEnterpriseName;

    /**
     * 物流商描述
     */
    @ApiModelProperty(name="shipmentEnterpriseDesc",value = "物流商描述")
    @Excel(name = "物流商描述", height = 20, width = 30,orderNum="3")
    @Column(name = "shipment_enterprise_desc")
    private String shipmentEnterpriseDesc;

    /**
     * 运输类型ID
     */
    @ApiModelProperty(name="transportCategoryId",value = "运输类型ID")
    @Excel(name = "运输类型ID", height = 20, width = 30,orderNum="4")
    @Column(name = "transport_category_id")
    @NotNull(message = "运输类型Id不能为空")
    private Byte transportCategoryId;

    /**
     * 联系人
     */
    @ApiModelProperty(name="linkman",value = "联系人")
    @Excel(name = "联系人", height = 20, width = 30)
    private String linkman;

    /**
     * 联系电话
     */
    @ApiModelProperty(name="phone",value = "联系电话")
    @Excel(name = "联系电话", height = 20, width = 30)
    private String phone;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
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
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

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
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}