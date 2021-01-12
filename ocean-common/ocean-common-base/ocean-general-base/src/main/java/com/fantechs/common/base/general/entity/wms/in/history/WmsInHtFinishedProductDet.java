package com.fantechs.common.base.general.entity.wms.in.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 成品入库单明细
 * wms_in_ht_finished_product_det
 * @author hyc
 * @date 2021-01-07 16:16:15
 */
@Data
@Table(name = "wms_in_ht_finished_product_det")
public class WmsInHtFinishedProductDet extends ValidGroup implements Serializable {
    /**
     * 履历ID
     */
    @ApiModelProperty(name="htFinishedProductDetId",value = "履历ID")
    @Excel(name = "履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_finished_product_det_id")
    @NotNull(groups = update.class,message = "履历ID")
    private Long htFinishedProductDetId;

    /**
     * 成品入库单明细ID
     */
    @ApiModelProperty(name="finishedProductDetId",value = "成品入库单明细ID")
    @Excel(name = "成品入库单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "finished_product_det_id")
    private Long finishedProductDetId;

    /**
     * 成品入库单ID
     */
    @ApiModelProperty(name="finishedProductId",value = "成品入库单ID")
    @Excel(name = "成品入库单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "finished_product_id")
    private String finishedProductId;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="productModelId",value = "产品ID")
    @Excel(name = "产品ID", height = 20, width = 30,orderNum="") 
    @Column(name = "product_model_id")
    private Long productModelId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Excel(name = "储位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 栈板编码
     */
    @ApiModelProperty(name="pallet_code",value = "栈板编码")
    @Excel(name = "栈板编码", height = 20, width = 30,orderNum="")
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 箱数
     */
    @ApiModelProperty(name="cartonQuantity",value = "箱数")
    @Excel(name = "箱数", height = 20, width = 30,orderNum="") 
    @Column(name = "carton_quantity")
    private BigDecimal cartonQuantity;

    /**
     * 入库数量
     */
    @ApiModelProperty(name="inQuantity",value = "入库数量")
    @Excel(name = "入库数量", height = 20, width = 30,orderNum="") 
    @Column(name = "in_quantity")
    private BigDecimal inQuantity;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

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

    @Excel(name = "成品编码", height = 20, width = 30,orderNum="")
    @ApiModelProperty(name="productMaterialCode" ,value="成品编码")
    private String productMaterialCode;

    @Excel(name = "成品名称", height = 20, width = 30,orderNum="")
    @ApiModelProperty(name="productMaterialName" ,value="成品名称")
    private String productMaterialName;

    @Excel(name = "成品描述", height = 20, width = 30,orderNum="")
    @ApiModelProperty(name="productMaterialName" ,value="成品描述（规格？）")
    private String productMaterialDesc;

    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="")
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="")
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    @Excel(name = "储位名称", height = 20, width = 30,orderNum="")
    @ApiModelProperty(name="storageName" ,value="储位名称")
    private String storageName;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode" ,value="组织编码")
    @Excel(name = "组织编码", height = 20, width = 30,orderNum="")
    private String organizationCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}