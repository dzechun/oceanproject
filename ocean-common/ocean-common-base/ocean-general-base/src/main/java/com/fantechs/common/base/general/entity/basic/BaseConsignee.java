package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 收货人信息
 * base_consignee
 * @author admin
 * @date 2021-04-23 16:10:30
 */
@Data
@Table(name = "base_consignee")
public class BaseConsignee extends ValidGroup implements Serializable {
    /**
     * 收货人信息ID
     */
    @ApiModelProperty(name="consigneeId",value = "收货人信息ID")
    @Excel(name = "收货人信息ID", height = 20, width = 30)
    @Id
    @Column(name = "consignee_id")
    @NotNull(groups = update.class,message = "收货人信息ID不能为空")
    private Long consigneeId;

    /**
     * 收货人编码
     */
    @ApiModelProperty(name="consigneeCode",value = "收货人编码")
    @Excel(name = "收货人编码", height = 20, width = 30)
    @Column(name = "consignee_code")
    @NotBlank(message = "收货人编码不能为空")
    private String consigneeCode;

    /**
     * 收货人名称
     */
    @ApiModelProperty(name="consigneeName",value = "收货人名称")
    @Excel(name = "收货人名称", height = 20, width = 30)
    @Column(name = "consignee_name")
    @NotBlank(message = "收货人名称不能为空")
    private String consigneeName;

    /**
     * 公司名称
     */
    @ApiModelProperty(name="companyName",value = "公司名称")
    @Excel(name = "公司名称", height = 20, width = 30)
    @Column(name = "company_name")
    private String companyName;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Excel(name = "货主ID", height = 20, width = 30)
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 联系人
     */
    @ApiModelProperty(name="linkManName",value = "联系人")
    @Excel(name = "联系人", height = 20, width = 30)
    @Column(name = "link_man_name")
    @NotBlank(message = "联系人不能为空")
    private String linkManName;

    /**
     * 联系电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系电话")
    @Excel(name = "联系电话", height = 20, width = 30)
    @Column(name = "link_man_phone")
    @NotBlank(message = "联系电话不能为空")
    private String linkManPhone;

    /**
     * 传真
     */
    @ApiModelProperty(name="faxNumber",value = "传真")
    @Excel(name = "传真", height = 20, width = 30)
    @Column(name = "fax_number")
    private String faxNumber;

    /**
     * 邮箱
     */
    @ApiModelProperty(name="emailAddress",value = "邮箱")
    @Excel(name = "邮箱", height = 20, width = 30)
    @Column(name = "e_mail_address")
    private String emailAddress;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="address",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30)
    private String address;

    /**
     * 描述
     */
    @ApiModelProperty(name="materialOwnerDesc",value = "描述")
    @Excel(name = "描述", height = 20, width = 30)
    @Column(name = "material_owner_desc")
    private String materialOwnerDesc;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,replace = {"无效_0","有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long orgId;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}