package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 产品族履历信息表
 * base_ht_product_family
 * @author 53203
 * @date 2020-12-15 15:56:46
 */
@Data
@Table(name = "base_ht_product_family")
public class BaseHtProductFamily implements Serializable {
    /**
     * 产品族履历ID
     */
    @ApiModelProperty(name="htProductFamilyId",value = "产品族履历ID")
    @Excel(name = "产品族履历ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_product_family_id")
    private Long htProductFamilyId;

    /**
     * 产品族id
     */
    @ApiModelProperty(name="productFamilyId",value = "产品族id")
    @Excel(name = "产品族id", height = 20, width = 30)
    @Column(name = "product_family_id")
    private Long productFamilyId;

    /**
     * 产品族编码
     */
    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    @Excel(name = "产品族编码", height = 20, width = 30)
    @Column(name = "product_family_code")
    private String productFamilyCode;

    /**
     * 产品族名称
     */
    @ApiModelProperty(name="productFamilyName",value = "产品族名称")
    @Excel(name = "产品族名称", height = 20, width = 30)
    @Column(name = "product_family_name")
    private String productFamilyName;

    /**
     * 产品族描述
     */
    @ApiModelProperty(name="productFamilyDesc",value = "产品族描述")
    @Excel(name = "产品族描述", height = 20, width = 30)
    @Column(name = "product_family_desc")
    private String productFamilyDesc;

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
     * 工厂（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "工厂（0、不启用 1、启用）")
    @Excel(name = "工厂（0、不启用 1、启用）", height = 20, width = 30)
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