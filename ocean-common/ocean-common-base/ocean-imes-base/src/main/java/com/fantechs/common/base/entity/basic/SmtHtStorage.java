package com.fantechs.common.base.entity.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_ht_storage")
@Data
public class SmtHtStorage {
    /**
     * 储位历史ID
     */
    @Id
    @Column(name = "ht_storage_id")
    @ApiModelProperty(name = "htStorageId",value = "储位历史ID")
    private Long htStorageId;

    /**
     * 储位ID
     */
    @Column(name = "storage_id")
    @ApiModelProperty(name = "storageId",value = "储位ID")
    private Long storageId;

    /**
     * 储位编码
     */
    @Column(name = "storage_code")
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageCode;

    /**
     * 储位名称
     */
    @Column(name = "storage_name")
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageName;

    /**
     * 储位描述
     */
    @Column(name = "storage_desc")
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageDesc;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id")
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private Long warehouseId;

    /**
     * 仓库区域ID
     */
    @Column(name = "warehouse_area_id")
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private Long warehouseAreaId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private Byte status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private Long createUserId;

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