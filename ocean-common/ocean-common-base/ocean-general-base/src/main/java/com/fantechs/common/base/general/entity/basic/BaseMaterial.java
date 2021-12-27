package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_material")
@Data
public class BaseMaterial extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 6879887744171421495L;
    /**
     * 物料ID
     */
    @Id
    @Column(name = "material_id")
    @NotNull(groups = update.class,message = "物料id不能为空")
    private Long materialId;

    /**
     * 物料编码
     */
    @Column(name = "material_code")
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    /**
     * 物料名称
     */
    @Column(name = "material_name")
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Column(name = "material_desc")
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 版本
     */
    @ApiModelProperty(name="materialVersion" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30)
    @Column(name = "material_version")
    private String materialVersion;

    /**
     * 基数
     */
    @ApiModelProperty(name="base" ,value="基数")
    private Integer base;

    /**
     * 物料来源(0.自制件 1.虚拟件 2.采购件)
     */
    @Column(name = "material_source")
    @ApiModelProperty(name="materialSource" ,value="物料来源(0.自制件 1.虚拟件 2.采购件)")
    @Excel(name = "物料来源", height = 20, width = 30,replace = {"自制件_0", "虚拟件_1","采购件_2"})
    private Integer materialSource;

    /**
     * 条码规则集合ID
     */
    @Column(name = "barcode_rule_set_id")
    @ApiModelProperty(name="barcodeRuleSetId" ,value="条码规则集合ID")
    private Long barcodeRuleSetId;

    /**
     * 系统来源
     */
    @Column(name = "system_source")
    @ApiModelProperty(name="systemSource" ,value="系统来源")
    private String systemSource;

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
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    @Column(name = "status")
    private Byte status;

    /**
     * 最小包装数
     */
    @Column(name = "min_package_number")
    @ApiModelProperty(name="minPackageNumber" ,value="最小包装数")
    private Integer minPackageNumber;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号ID")
    private Long createUserId;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号ID")
    private Long modifiedUserId;


    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @Column(name = "option1")
    private String option1;

    /**
     * 扩展字段2
     */
    @Column(name = "option2")
    private String option2;

    /**
     * 扩展字段3
     */
    @Column(name = "option3")
    private String option3;

    /**
     * 页签
     */
    @Transient
    @ApiModelProperty(name = "baseTabDto",value = "页签")
    private BaseTabDto baseTabDto;
}
