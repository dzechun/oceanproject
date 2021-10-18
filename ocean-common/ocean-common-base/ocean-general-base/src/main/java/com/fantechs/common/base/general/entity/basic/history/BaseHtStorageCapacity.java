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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 库容设置履历表
 * base_ht_storage_capacity
 * @author admin
 * @date 2021-10-18 10:06:52
 */
@Data
@Table(name = "base_ht_storage_capacity")
public class BaseHtStorageCapacity extends ValidGroup implements Serializable {
    /**
     * 库容设置履历ID
     */
    @ApiModelProperty(name="htStorageCapacityId",value = "库容设置履历ID")
    @Excel(name = "库容设置履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_storage_capacity_id")
    private Long htStorageCapacityId;

    /**
     * 库容设置ID
     */
    @ApiModelProperty(name="storageCapacityId",value = "库容设置ID")
    @Excel(name = "库容设置ID", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_capacity_id")
    private Long storageCapacityId;

    /**
     * 物料编码ID
     */
    @ApiModelProperty(name="materialId",value = "物料编码ID")
    @Excel(name = "物料编码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * A类容量
     */
    @ApiModelProperty(name="typeACapacity",value = "A类容量")
    @Excel(name = "A类容量", height = 20, width = 30,orderNum="") 
    @Column(name = "type_a_capacity")
    private BigDecimal typeACapacity;

    /**
     * B类容量
     */
    @ApiModelProperty(name="typeBCapacity",value = "B类容量")
    @Excel(name = "B类容量", height = 20, width = 30,orderNum="") 
    @Column(name = "type_b_capacity")
    private BigDecimal typeBCapacity;

    /**
     * C类容量
     */
    @ApiModelProperty(name="typeCCapacity",value = "C类容量")
    @Excel(name = "C类容量", height = 20, width = 30,orderNum="") 
    @Column(name = "type_c_capacity")
    private BigDecimal typeCCapacity;

    /**
     * D类容量
     */
    @ApiModelProperty(name="typeDCapacity",value = "D类容量")
    @Excel(name = "D类容量", height = 20, width = 30,orderNum="") 
    @Column(name = "type_d_capacity")
    private BigDecimal typeDCapacity;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
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
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="1")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="2")
    private String materialName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum = "5")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum = "7")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}