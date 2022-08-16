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
 * 免检清单履历表
 * base_ht_inspection_exempted_list
 * @author admin
 * @date 2021-05-21 15:43:15
 */
@Data
@Table(name = "base_ht_inspection_exempted_list")
public class BaseHtInspectionExemptedList extends ValidGroup implements Serializable {
    /**
     * 免检清单履历ID
     */
    @ApiModelProperty(name="htInspectionExemptedListId",value = "免检清单履历ID")
    @Excel(name = "免检清单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_inspection_exempted_list_id")
    private Long htInspectionExemptedListId;

    /**
     * 免检清单ID
     */
    @ApiModelProperty(name="inspectionExemptedListId",value = "免检清单ID")
    @Excel(name = "免检清单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_exempted_list_id")
    private Long inspectionExemptedListId;

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
    @ApiModelProperty(name="customerId",value = "客户ID")
    @Excel(name = "客户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 类型(1-供应商 2-客户)
     */
    @ApiModelProperty(name="objType",value = "类型(1-供应商 2-客户)")
    @Excel(name = "类型(1-供应商 2-客户)", height = 20, width = 30,orderNum="") 
    @Column(name = "obj_type")
    private Byte objType;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 供应商编码
     */
    @Transient
    @ApiModelProperty(name = "supplierCode",value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 客户编码
     */
    @Transient
    @ApiModelProperty(name = "customerCode",value = "客户编码")
    private String customerCode;

    /**
     * 客户名称
     */
    @Transient
    @ApiModelProperty(name = "customerName",value = "客户名称")
    private String customerName;

    private static final long serialVersionUID = 1L;
}