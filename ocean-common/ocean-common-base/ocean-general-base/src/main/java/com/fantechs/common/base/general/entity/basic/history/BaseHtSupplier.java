package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 供应商履历表
 * base_ht_supplier
 * @author admin
 * @date 2021-10-26 09:57:04
 */
@Data
@Table(name = "base_ht_supplier")
public class BaseHtSupplier extends ValidGroup implements Serializable {
    /**
     * 供应商履历ID
     */
    @ApiModelProperty(name="htSupplierId",value = "供应商履历ID")
    @Excel(name = "供应商履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_supplier_id")
    private Long htSupplierId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "supplier_code")
    private String supplierCode;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "supplier_desc")
    private String supplierDesc;

    /**
     * 地址ID
     */
    @ApiModelProperty(name="addressId",value = "地址ID")
    @Excel(name = "地址ID", height = 20, width = 30,orderNum="") 
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "supplier_abbreviation")
    private String supplierAbbreviation;

    @Column(name = "supplier_logo")
    private String supplierLogo;

    @Column(name = "liaison_man")
    private String liaisonMan;

    private String telephone;

    /**
     * 身份标识（1、供应商 2、客户 3、门店）
     */
    @ApiModelProperty(name="supplierType",value = "身份标识（1、供应商 2、客户 3、门店）")
    @Excel(name = "身份标识（1、供应商 2、客户 3、门店）", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_type")
    private Byte supplierType;

    /**
     * 国家名称
     */
    @ApiModelProperty(name="countryName",value = "国家名称")
    @Excel(name = "国家名称", height = 20, width = 30)
    @Column(name = "country_name")
    private String countryName;

    /**
     * 大区名称
     */
    @ApiModelProperty(name="regionName",value = "大区名称")
    @Excel(name = "大区名称", height = 20, width = 30)
    @Column(name = "region_name")
    private String regionName;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long organizationId;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}