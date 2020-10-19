package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "smt_product_bom_det")
@Data
public class SmtProductBomDet extends ValidGroup implements Serializable {
    private static final long serialVersionUID = -8895517961127531303L;
    /**
     * 产品BOM详细ID
     */
    @Id
    @Column(name = "product_bom_det_id")
    @ApiModelProperty(name="productBomDetId" ,value="产品BOM详细ID")
    @NotNull(groups = update.class,message = "产品BOM详细ID不能为空")
    private Long productBomDetId;

    /**
     * 产品BOM ID
     */
    @Column(name = "product_bom_id")
    @ApiModelProperty(name="productBomId" ,value="产品BOM ID")
    @NotNull(message = "产品BOM ID不能为空")
    private Long productBomId;

    /**
     * 物料ID
     */
    @Column(name = "part_material_id")
    @ApiModelProperty(name="partMaterialId" ,value="物料ID")
    @NotNull(message = "产品物料ID不能为空")
    private Long partMaterialId;

    /**
     * 零件料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="零件料号")
    @Excel(name = "零件料号", height = 20, width = 30)
    private String partMaterialCode;

    /**
     * 零件名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="零件名称")
    private String materialName;

    /**
     * 零件料号版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="零件料号版本")
    @Excel(name = "零件料号版本", height = 20, width = 30)
    private String version;

    /**
     * 零件料号描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="零件料号描述")
    @Excel(name = "零件料号描述", height = 20, width = 30)
    private String materialDesc;


    /**
     * 代用物料ID
     */
    @Column(name = "sub_material_id")
    @ApiModelProperty(name="subMaterialId" ,value="代用物料ID")
    private Long subMaterialId;

    /**
     * 代用料号
     */
    @Transient
    @ApiModelProperty(name="subMaterialCode" ,value="代用料号")
    @Excel(name = "代用料号", height = 20, width = 30)
    private String subMaterialCode;

    /**
     * 工序ID
     */
    @Column(name = "process_id")
    @ApiModelProperty(name="processId" ,value="工序ID")
    @NotNull(message = "工序ID不能为空")
    private Long processId;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30)
    private String processName;
    /**
     * 用量
     */
    @ApiModelProperty(name="quantity" ,value="用量")
    @Excel(name = "用量", height = 20, width = 30)
    private BigDecimal quantity;

    /**
     * 位置
     */
    @ApiModelProperty(name="position" ,value="位置")
    @Excel(name = "位置", height = 20, width = 30)
    private String position;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
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
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
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
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除")
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