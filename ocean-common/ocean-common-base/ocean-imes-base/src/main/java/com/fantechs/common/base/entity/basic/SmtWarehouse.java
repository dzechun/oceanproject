package com.fantechs.common.base.entity.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_warehouse")
@Data
public class SmtWarehouse implements Serializable {
    /**
     * 仓库ID
     */
    @Id
    @Column(name = "warehouse_id")
    @ApiModelProperty(name = "warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 仓库编码
     */
    @Column(name = "warehouse_code")
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Column(name = "warehouse_name")
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库描述
     */
    @Column(name = "warehouse_desc")
    @ApiModelProperty(name = "warehouseDesc",value = "仓库描述")
    private String warehouseDesc;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    private Byte status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId",value = "创建人ID")
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