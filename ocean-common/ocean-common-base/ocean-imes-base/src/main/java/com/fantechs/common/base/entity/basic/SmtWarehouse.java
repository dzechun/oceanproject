package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "smt_warehouse")
@Data
public class SmtWarehouse extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 3791280545481754788L;
    /**
     * 仓库ID
     */
    @Id
    @Column(name = "warehouse_id")
    @ApiModelProperty(name = "warehouseId",value = "仓库ID")
    @NotNull(groups = update.class,message = "仓库id不能为空")
    private Long warehouseId;

    /**
     * 仓库编码
     */
    @Column(name = "warehouse_code")
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30)
    @NotBlank(message = "仓库编码不能为空")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Column(name = "warehouse_name")
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30)
    @NotBlank(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 仓库描述
     */
    @Column(name = "warehouse_desc")
    @ApiModelProperty(name = "warehouseDesc",value = "仓库描述")
    @Excel(name = "仓库描述", height = 20, width = 30)
    private String warehouseDesc;

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