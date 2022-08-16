package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "base_ht_warehouse")
@Data
public class BaseHtWarehouse implements Serializable {

    private static final long serialVersionUID = -4316654721930294111L;
    /**
     * 仓库历史ID
     */
    @Id
    @Column(name = "ht_warehouse_id")
    @ApiModelProperty(name = "htWarehouseId",value = "仓库历史ID")
    private Long htWarehouseId;

    /**
     * 仓库ID
     */
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
     * 仓库类型
     */
    @ApiModelProperty(name="warehouseCategory",value = "仓库类型")
    @Excel(name = "仓库类型", height = 20, width = 30)
    @Column(name = "warehouse_category")
    private Long warehouseCategory;

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
     * 联系人
     */
    @ApiModelProperty(name="linkManName",value = "联系人")
    @Excel(name = "联系人", height = 20, width = 30)
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系电话")
    @Excel(name = "联系电话", height = 20, width = 30)
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 传真号码
     */
    @ApiModelProperty(name="faxNumber",value = "传真号码")
    @Excel(name = "传真号码", height = 20, width = 30)
    @Column(name = "fax_number")
    private String faxNumber;

    /**
     * 地址
     */
    @ApiModelProperty(name="address",value = "地址")
    @Excel(name = "地址", height = 20, width = 30)
    @Column(name = "address")
    private String address;

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
