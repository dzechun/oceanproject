package com.fantechs.common.base.general.entity.basic.history;

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
import java.util.Date;

;
;

/**
 * 检验标准履历表
 * base_ht_inspection_standard
 * @author admin
 * @date 2021-05-19 11:44:04
 */
@Data
@Table(name = "base_ht_inspection_standard")
public class BaseHtInspectionStandard extends ValidGroup implements Serializable {
    /**
     * 检验标准履历ID
     */
    @ApiModelProperty(name="htInspectionStandardId",value = "检验标准履历ID")
    @Excel(name = "检验标准履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_inspection_standard_id")
    private Long htInspectionStandardId;

    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId",value = "检验标准ID")
    @Excel(name = "检验标准ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_standard_id")
    private Long inspectionStandardId;

    /**
     * 检验标准编码
     */
    @ApiModelProperty(name="inspectionStandardCode",value = "检验标准编码")
    @Excel(name = "检验标准编码", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_standard_code")
    private String inspectionStandardCode;

    /**
     * 检验标准名称
     */
    @ApiModelProperty(name="inspectionStandardName",value = "检验标准名称")
    @Excel(name = "检验标准名称", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_standard_name")
    private String inspectionStandardName;

    /**
     * 检验标准版本号
     */
    @ApiModelProperty(name="inspectionStandardVersion",value = "检验标准版本号")
    @Excel(name = "检验标准版本号", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_standard_version")
    private String inspectionStandardVersion;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Excel(name = "客户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 检验类型(1- 来料检验 2- 驻厂检验 3-出货检验)
     */
    @ApiModelProperty(name="inspectionType",value = "检验类型(1- 来料检验 2- 驻厂检验 3-出货检验)")
    @Excel(name = "检验类型(1- 来料检验 2- 驻厂检验 3-出货检验)", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_type")
    private Byte inspectionType;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    @Column(name = "inspection_way_id")
    private Long inspectionWayId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
    @Transient
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="5")
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
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="7")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Transient
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="7")
    private String materialName;

    /**
     * 客户
     */
    @ApiModelProperty(name="supplierName" ,value="客户")
    @Transient
    @Excel(name = "客户", height = 20, width = 30,orderNum="7")
    private String supplierName;

    /**
     * 检验方式
     */
    @ApiModelProperty(name="inspectionWayCode" ,value="检验方式")
    @Transient
    @Excel(name = "检验方式", height = 20, width = 30,orderNum="7")
    private String inspectionWayCode;

    private static final long serialVersionUID = 1L;
}