package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 单据类型信息履历表
 * base_ht_order_type
 * @author 53203
 * @date 2021-04-26 17:36:23
 */
@Data
@Table(name = "base_ht_order_type")
public class BaseHtOrderType extends ValidGroup implements Serializable {
    /**
     * 单据类型履历ID
     */
    @ApiModelProperty(name="htOrderTypeId",value = "单据类型履历ID")
    @Excel(name = "单据类型履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_order_type_id")
    private Long htOrderTypeId;

    /**
     * 单据类型ID
     */
    @ApiModelProperty(name="orderTypeId",value = "单据类型ID")
    @Excel(name = "单据类型ID", height = 20, width = 30,orderNum="") 
    @Column(name = "order_type_id")
    private Long orderTypeId;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据类型编码")
    @Excel(name = "单据类型编码", height = 20, width = 30,orderNum="") 
    @Column(name = "order_type_code")
    private String orderTypeCode;

    /**
     * 单据类型名称
     */
    @ApiModelProperty(name="orderTypeName",value = "单据类型名称")
    @Excel(name = "单据类型名称", height = 20, width = 30,orderNum="") 
    @Column(name = "order_type_name")
    private String orderTypeName;

    /**
     * 业务类型
     */
    @ApiModelProperty(name="businessType",value = "业务类型")
    @Excel(name = "业务类型", height = 20, width = 30,orderNum="") 
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Excel(name = "货主ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

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
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="4")
    private String warehouseName;

    /**
     * 仓库编码
     */
    @Transient
    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="5")
    private String warehouseCode;

    /**
     * 货主编码
     */
    @Transient
    @ApiModelProperty(name="materialOwnerCode",value = "货主编码")
    @Excel(name = "货主编码", height = 20, width = 30,orderNum="6")
    private String materialOwnerCode;

    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="7")
    private String materialOwnerName;

    /**
     * 货主简称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerShortName",value = "货主简称")
    @Excel(name = "货主简称", height = 20, width = 30,orderNum="8")
    private String materialOwnerShortName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;

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