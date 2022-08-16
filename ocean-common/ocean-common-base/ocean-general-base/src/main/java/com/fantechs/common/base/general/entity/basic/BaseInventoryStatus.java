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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 库存状态
 * base_inventory_status
 * @author admin
 * @date 2021-04-25 14:57:50
 */
@Data
@Table(name = "base_inventory_status")
public class BaseInventoryStatus extends ValidGroup implements Serializable {
    /**
     * 单据类型ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "单据类型ID")
    @Id
    @Column(name = "inventory_status_id")
    @NotNull(groups = update.class,message = "单据类型ID不能为空")
    private Long inventoryStatusId;

    /**
     * 状态编码
     */
    @ApiModelProperty(name="inventoryStatusCode",value = "状态编码")
    @Column(name = "inventory_status_code")
    private String inventoryStatusCode;

    /**
     * 状态名称
     */
    @ApiModelProperty(name="inventoryStatusName",value = "状态名称")
    @Excel(name = "状态名称", height = 20, width = 30,orderNum="3")
    @Column(name = "inventory_status_name")
    private String inventoryStatusName;

    /**
     * 是否可发(0-否 1-是)
     */
    @ApiModelProperty(name="ifCanStoreIssue",value = "是否可发(0-否 1-是)")
    @Excel(name = "是否可发(0-否 1-是)", height = 20, width = 30,orderNum="4",replace = {"否_0", "是_1"})
    @Column(name = "if_can_store_issue")
    private Byte ifCanStoreIssue;

    /**
     * 是否默认状态(0-否 1-是)
     */
    @ApiModelProperty(name="ifDefaultStatus",value = "是否默认状态(0-否 1-是)")
    @Column(name = "if_default_status")
    private Byte ifDefaultStatus;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="6",replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="5")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
    @Transient
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName" ,value="货主名称")
    @Transient
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="2")
    private String materialOwnerName;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Transient
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="1")
    private String warehouseName;

}