package com.fantechs.common.base.general.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "base_ht_storage_material")
@Data
public class BaseHtStorageMaterial implements Serializable {
    private static final long serialVersionUID = 8334165591357177792L;
    /**
     * 储位物料ID
     */
    @Id
    @Column(name = "ht_storage_material_id")
    private Long htStorageMaterialId;

    /**
     * 储位物料ID
     */
    @Column(name = "storage_material_id")
    @ApiModelProperty(name = "storageMaterialId",value = "储位物料ID")
    private Long storageMaterialId;

    /**
     * 储位ID
     */
    @Column(name = "storage_id")
    @ApiModelProperty(name = "storageId",value = "储位ID")
    private Long storageId;

    /**
     * 储位编码
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageCode;

    /**
     * 储位名称
     */
    @Transient
    @ApiModelProperty(name = "storageName",value = "储位名称")
    private String storageName;

    /**
     * 储位描述
     */
    @Transient
    @ApiModelProperty(name = "storageDesc",value = "储位描述")
    private String storageDesc;

    /**
     * 物料ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name = "materialId",value = "物料ID")
    private Long materialId;

    /**
     * 货主ID
     */
    @ApiModelProperty(name = "materialOwnerId",value = "货主ID")
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name = "materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 上架策略
     */
    @Column(name = "putaway_tactics")
    @ApiModelProperty(name = "putawayTactics",value = "上架策略")
    private Byte putawayTactics;

    /**
     * 补货策略
     */
    @Column(name = "replenish_tactics")
    @ApiModelProperty(name = "replenishTactics",value = "补货策略")
    private Byte replenishTactics;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;


    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

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
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name = "modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name = "isDelete",value = "逻辑删除")
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
}
