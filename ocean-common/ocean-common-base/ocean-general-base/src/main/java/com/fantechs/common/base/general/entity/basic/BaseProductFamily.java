package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 产品族信息表
 * base_product_family
 * @author 53203
 * @date 2020-12-15 15:56:45
 */
@Data
@Table(name = "base_product_family")
public class BaseProductFamily extends ValidGroup implements Serializable {
    /**
     * 产品族id
     */
    @ApiModelProperty(name="productFamilyId",value = "产品族id")
    @Id
    @Column(name = "product_family_id")
    @NotNull(groups = update.class,message = "产品族id不能为空")
    private Long productFamilyId;

    /**
     * 产品族编码
     */
    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    @Excel(name = "产品族编码", height = 20, width = 30,orderNum="1")
    @Column(name = "product_family_code")
    @NotBlank(message = "产品族编码不能为空")
    private String productFamilyCode;

    /**
     * 产品族名称
     */
    @ApiModelProperty(name="productFamilyName",value = "产品族名称")
    @Excel(name = "产品族名称", height = 20, width = 30,orderNum="2")
    @Column(name = "product_family_name")
    @NotBlank(message = "产品族名称不能为空")
    private String productFamilyName;

    /**
     * 产品族描述
     */
    @ApiModelProperty(name="productFamilyDesc",value = "产品族描述")
    @Excel(name = "产品族描述", height = 20, width = 30,orderNum="3")
    @Column(name = "product_family_desc")
    private String productFamilyDesc;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30,orderNum="4",replace = {"不启用_0","启用_1"})
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="5",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}