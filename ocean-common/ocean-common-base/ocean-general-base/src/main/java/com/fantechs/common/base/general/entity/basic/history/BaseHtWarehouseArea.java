package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "base_ht_warehouse_area")
@Data
public class BaseHtWarehouseArea implements Serializable {
    private static final long serialVersionUID = 529331700988362697L;
    /**
     * 仓库区域历史ID
     */
    @Id
    @Column(name = "ht_warehouse_area_id")
    @ApiModelProperty(name="htWarehouseAreaId" ,value="仓库区域历史ID")
    private Long htWarehouseAreaId;

    @Column(name = "warehouse_area_id")
    @ApiModelProperty(name="warehouseAreaId" ,value="仓库区域ID")
    private Long warehouseAreaId;

    /**
     * 仓库区域编码
     */
    @Column(name = "warehouse_area_code")
    @ApiModelProperty(name="warehouseAreaCode" ,value="仓库区域编码")
    private String warehouseAreaCode;

    /**
     * 仓库区域名称
     */
    @Column(name = "warehouse_area_name")
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    /**
     * 仓库区域描述
     */
    @Column(name = "warehouse_area_desc")
    @ApiModelProperty(name="warehouseAreaDesc" ,value="仓库区域描述")
    private String warehouseAreaDesc;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id")
    @ApiModelProperty(name="warehouseId" ,value="仓库ID")
    private Long warehouseId;

    /**
     * 容量
     */
    @ApiModelProperty(name="capacity",value = "容量")
    @Excel(name = "容量", height = 20, width = 30)
    private BigDecimal capacity;

    /**
     * 温度
     */
    @ApiModelProperty(name="temperature",value = "温度")
    @Excel(name = "温度", height = 20, width = 30)
    private BigDecimal temperature;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit",value = "单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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
    @ApiModelProperty(name="status" ,value="状态(0无效，1有效)")
    private Byte status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
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


    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String  warehouseName;
}
