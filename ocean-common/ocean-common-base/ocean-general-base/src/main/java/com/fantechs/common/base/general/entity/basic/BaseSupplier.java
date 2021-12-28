package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "base_supplier")
@Data
public class BaseSupplier extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 379038968866477984L;
    /**
     * 供应商ID
     */
    @Id
    @Column(name = "supplier_id")
    @ApiModelProperty("供应商ID")
    @NotNull(groups = update.class,message = "id不能为空")
    private Long supplierId;

    /**
     * 供应商代码
     */
    @Column(name = "supplier_code")
    @ApiModelProperty("供应商(客户)代码")
    @Excel(name = "供应商(客户)代码", height = 20, width = 30)
    @NotBlank(message = "编码不能为空")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @Column(name = "supplier_name")
    @ApiModelProperty("供应商(客户)名称")
    @Excel(name = "供应商(客户)名称", height = 20, width = 30)
    @NotBlank(message = "名称不能为空")
    private String supplierName;

    /**
     * 供应商描述
     */
    @Column(name = "supplier_desc")
    @ApiModelProperty("供应商(客户)描述")
    @Excel(name = "供应商(客户)描述", height = 20, width = 30)
    private String supplierDesc;

    /**
     * 简称
     */
    @Column(name = "supplier_abbreviation")
    @ApiModelProperty("简称")
    @Excel(name = "简称", height = 20, width = 30)
    private String supplierAbbreviation;

    /**
     * LOGO
     */
    @Column(name = "supplier_logo")
    @ApiModelProperty("LOGO")
    @Excel(name = "LOGO", height = 20, width = 30)
    private String supplierLogo;

    /**
     * 联系人
     */
    @Column(name = "liaison_man")
    @ApiModelProperty("联系人")
    @Excel(name = "联系人", height = 20, width = 30)
    private String liaisonMan;

    /**
     * 联系电话
     */
    @Column(name = "telephone")
    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话", height = 20, width = 30)
    private String telephone;

    /**
     * 邮箱
     */
    @Column(name = "e_mail")
    @ApiModelProperty("邮箱")
    @Excel(name = "邮箱", height = 20, width = 30)
    private String email;

    /**
     * 手机号
     */
    @Column(name = "mobile_phone")
    @ApiModelProperty("手机号")
    @Excel(name = "手机号", height = 20, width = 30)
    private String mobilePhone;

    /**
     * 是否预约发货(0-否 1-是)
     */
    @Column(name = "if_appoint_deliver")
    @ApiModelProperty("是否预约发货(0-否 1-是)")
    @Excel(name = "是否预约发货(0-否 1-是)", height = 20, width = 30,replace = {"否_0","是_1"})
    private Byte ifAppointDeliver;

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
    @ApiModelProperty("状态(0无效，1有效)")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0","有效_1"})
    private Byte status;

    /**
     * 身份标识（1、供应商 2、客户）
     */
    @Column(name = "supplier_type")
    @ApiModelProperty("身份标识（1、供应商 2、客户）")
    @Excel(name = "身份标识", height = 20, width = 30,replace = {"供应商_1","客户_2"})
    private Byte supplierType;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty("创建人Id")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty("修改人id")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty("逻辑删除（0、删除 1、正常）")
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
     * 地址集合
     */
    @ApiModelProperty("地址集合")
    private List<BaseAddressDto> list;
}
