package com.fantechs.common.base.general.entity.bcm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 安全库存
 * olt_safe_stock
 * @author mr.lei
 * @date 2021-03-04 14:04:50
 */
@Data
@Table(name = "olt_safe_stock")
public class OltSafeStock extends ValidGroup implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(name="safeStockId",value = "id")
    @Excel(name = "id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "safe_stock_id")
    private Long safeStockId;

    /**
     * 安全库存类型1：仓库、2:物料类型、3:产品料号
     */
    @ApiModelProperty(name="safeStockType",value = "安全库存类型1：仓库、2:物料类型、3:产品料号")
    @Excel(name = "安全库存类型1：仓库、2:物料类型、3:产品料号", height = 20, width = 30,orderNum="") 
    @Column(name = "safe_stock_type")
    private Byte safeStockType;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    @Excel(name = "仓库id", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 物料类别id
     */
    @ApiModelProperty(name="materialCategoryId",value = "物料类别id")
    @Excel(name = "物料类别id", height = 20, width = 30,orderNum="") 
    @Column(name = "material_category_id")
    private Long materialCategoryId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    @Excel(name = "物料id", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 安全库存数
     */
    @ApiModelProperty(name="safeStockQuantity",value = "安全库存数")
    @Excel(name = "安全库存数", height = 20, width = 30,orderNum="") 
    @Column(name = "safe_stock_quantity")
    private BigDecimal safeStockQuantity;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="") 
    private String option3;

    private static final long serialVersionUID = 1L;
}