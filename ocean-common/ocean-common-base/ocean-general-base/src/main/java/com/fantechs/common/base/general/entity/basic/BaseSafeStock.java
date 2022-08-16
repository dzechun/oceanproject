package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 安全库存
 * base_safe_stock
 * @author mr.lei
 * @date 2021-03-04 14:04:50
 */
@Data
@Table(name = "base_safe_stock")
public class BaseSafeStock extends ValidGroup implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(name="safeStockId",value = "id")
    @Id
    @Column(name = "safe_stock_id")
    @NotNull(groups = update.class,message = "id不能为空")
    private Long safeStockId;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    @Column(name = "warehouse_id")
    @NotNull(message = "仓库id不能为空")
    private Long warehouseId;

    /**
     * 物料类别id
     */
    @ApiModelProperty(name="materialCategoryId",value = "物料类别id")
    @Column(name = "material_category_id")
    private Long materialCategoryId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Column(name = "material_owner_id")
    @NotNull(message = "货主ID不能为空")
    private Long materialOwnerId;

    /**
     * 最小库存数
     */
    @ApiModelProperty(name="minQty",value = "最小库存数")
    @Excel(name = "最小库存数", height = 20, width = 30,orderNum="7")
    @Column(name = "min_qty")
    @NotNull(message = "最小库存数不能为空")
    private BigDecimal minQty;

    /**
     * 最大数
     */
    @ApiModelProperty(name="maxQty",value = "最小数")
    @Excel(name = "最大数", height = 20, width = 30,orderNum="8")
    @Column(name = "max_qty")
    @NotNull(message = "最大库存数不能为空")
    private BigDecimal maxQty;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    private static final long serialVersionUID = 1L;
}