package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_storage")
@Data
public class SmtStorage {
    /**
     * 储位ID
     */
    @Id
    @Column(name = "storage_id")
    @ApiModelProperty(name = "storageId",value = "储位ID")
    private Long storageId;

    /**
     * 储位编码
     */
    @Column(name = "storage_code")
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30)
    private String storageCode;

    /**
     * 储位名称
     */
    @Column(name = "storage_name")
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30)
    private String storageName;

    /**
     * 储位描述
     */
    @Column(name = "storage_desc")
    @ApiModelProperty(name = "storageDesc",value = "储位描述")
    @Excel(name = "储位描述", height = 20, width = 30)
    private String storageDesc;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id")
    @ApiModelProperty(name = "warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30)
    private String warehouseName;

    /**
     * 仓库区域ID
     */
    @Column(name = "warehouse_area_id")
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域ID")
    private Long warehouseAreaId;

    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30)
    private String warehouseAreaName;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
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