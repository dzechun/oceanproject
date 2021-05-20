package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 检验标准
 * base_inspection_standard
 * @author admin
 * @date 2021-05-19 11:44:03
 */
@Data
@Table(name = "base_inspection_standard")
public class BaseInspectionStandard extends ValidGroup implements Serializable {
    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId",value = "检验标准ID")
    @Id
    @Column(name = "inspection_standard_id")
    @NotNull(groups = update.class,message = "检验标准ID不能为空")
    private Long inspectionStandardId;

    /**
     * 检验标准编码
     */
    @ApiModelProperty(name="inspectionStandardCode",value = "检验标准编码")
    @Excel(name = "检验标准编码", height = 20, width = 30,orderNum="1")
    @Column(name = "inspection_standard_code")
    @NotBlank(message = "检验标准编码不能为空")
    private String inspectionStandardCode;

    /**
     * 检验标准名称
     */
    @ApiModelProperty(name="inspectionStandardName",value = "检验标准名称")
    @Excel(name = "检验标准名称", height = 20, width = 30,orderNum="2")
    @Column(name = "inspection_standard_name")
    private String inspectionStandardName;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 检验类型ID
     */
    @ApiModelProperty(name="inspectionTypeId",value = "检验类型ID")
    @Column(name = "inspection_type_id")
    private Long inspectionTypeId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="7")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Transient
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="3")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Transient
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="4")
    private String materialName;

    /**
     * 客户
     */
    @ApiModelProperty(name="supplierName" ,value="客户")
    @Transient
    @Excel(name = "客户", height = 20, width = 30,orderNum="5")
    private String supplierName;

    /**
     * 检验类型
     */
    @ApiModelProperty(name="inspectionTypeName" ,value="检验类型")
    @Transient
    @Excel(name = "检验类型", height = 20, width = 30,orderNum="6")
    private String inspectionTypeName;

    /**
     * 检验标准明细
     */
    @ApiModelProperty(name="baseInspectionStandardDets",value = "检验标准明细")
    private List<BaseInspectionStandardDet> baseInspectionStandardDets = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}