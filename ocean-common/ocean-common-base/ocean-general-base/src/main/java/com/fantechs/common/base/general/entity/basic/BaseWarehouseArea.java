package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "base_warehouse_area")
@Data
public class BaseWarehouseArea extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 6313098859931139882L;
    /**
     * 仓库区域ID
     */
    @Id
    @Column(name = "warehouse_area_id")
    @ApiModelProperty(name="warehouseAreaId" ,value="仓库区域ID")
    @NotNull(groups = update.class,message = "仓库区域id不能为空")
    private Long warehouseAreaId;

    /**
     * 仓库区域编码
     */
    @Column(name = "warehouse_area_code")
    @ApiModelProperty(name="warehouseAreaCode" ,value="仓库区域编码")
    @Excel(name = "仓库区域编码", height = 20, width = 30,orderNum="1")
    @NotBlank(message = "仓库区域编码不能为空")
    private String warehouseAreaCode;

    /**
     * 仓库区域名称
     */
    @Column(name = "warehouse_area_name")
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="2")
    @NotBlank(message = "仓库区域名称不能为空")
    private String warehouseAreaName;

    /**
     * 仓库区域描述
     */
    @Column(name = "warehouse_area_desc")
    @ApiModelProperty(name="warehouseAreaDesc" ,value="仓库区域描述")
    @Excel(name = "仓库区域描述", height = 20, width = 30,orderNum="3")
    private String warehouseAreaDesc;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id")
    @ApiModelProperty(name="warehouseId" ,value="仓库ID")
    @NotNull(message = "仓库id不能为空")
    private Long warehouseId;

    /**
     * 最大使用率
     */
    @ApiModelProperty(name="max_usage_rate",value = "最大使用率(%)")
    @Excel(name = "最大使用率(%)", height = 20, width = 30)
    private BigDecimal maxUsageRate;

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
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status" ,value="状态(0无效，1有效)")
    @Excel(name = "仓库状态", height = 20, width = 30 ,orderNum="5",replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat = "yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat = "yyyy-MM-dd HH:mm:ss")
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

    @ApiModelProperty(name = "logicId",value = "erp逻辑id")
    @Column(name = "logic_id")
    private Long logicId;

   }
