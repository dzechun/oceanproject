package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
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

@Table(name = "base_product_model")
@Data
public class BaseProductModel extends ValidGroup implements Serializable {

    private static final long serialVersionUID = -3520314696299928511L;
    /**
     *  产品型号ID
     */
    @Id
    @Column(name = "product_model_id")
    @ApiModelProperty(name="productModelId" ,value="产品型号ID")
    @NotNull(groups = update.class,message = "产品型号id不能为空")
    private Long productModelId;

    /**
     *  产品型号编码
     */
    @Column(name = "product_model_code")
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Excel(name = "产品型号", height = 20, width = 30)
    @NotBlank(message = "产品型号编码不能为空")
    private String productModelCode;

    /**
     *  产品型号名称
     */
    @Column(name = "product_model_name")
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    @Excel(name = "产品型号名称", height = 20, width = 30)
    private String productModelName;

    /**
     *  产品型号描述
     */
    @Column(name = "product_model_desc")
    @ApiModelProperty(name="productModelDesc" ,value="产品型号描述")
    @Excel(name = "产品型号描述", height = 20, width = 30)
    private String productModelDesc;

    /**
     * 产品族id
     */
    @ApiModelProperty(name="productFamilyId",value = "产品族id")
    @Column(name = "product_family_id")
    private Long productFamilyId;

    /**
     * 产品族编码
     */
    @Transient
    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    @Excel(name = "产品族编码", height = 20, width = 30)
    private String productFamilyCode;

    /**
     * 产品族名称
     */
    @Transient
    @ApiModelProperty(name="productFamilyName",value = "产品族名称")
    @Excel(name = "产品族名称", height = 20, width = 30)
    private String productFamilyName;

    /**
     * 产品族描述
     */
    @Transient
    @ApiModelProperty(name="productFamilyDesc",value = "产品族描述")
    @Excel(name = "产品族描述", height = 20, width = 30)
    private String productFamilyDesc;

    /**
     * 版本
     */
    @ApiModelProperty(name="version",value = "版本")
    @Column(name = "version")
    private String version;

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
    @ApiModelProperty(name="status" ,value="状态（0、无效 1、有效）")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @Excel(name = "创建账号", height = 20, width = 30)
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号")
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
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
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
     * 组织名称
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Transient
    private Long materialId;
}
