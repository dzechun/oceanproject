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
    @Excel(name = "履历ID", height = 20, width = 30) 
    @Id
    @Column(name = "ht_finished_product_det_id")
    @NotNull(groups = update.class,message = "履历ID")
    private Long htFinishedProductDetId;

    /**
     * 成品入库单明细ID
     */
    @ApiModelProperty(name="finishedProductDetId",value = "成品入库单明细ID")
    @Excel(name = "成品入库单明细ID", height = 20, width = 30) 
    @Column(name = "finished_product_det_id")
    private Long finishedProductDetId;

    /**
     * 成品入库单ID
     */
    @ApiModelProperty(name="finishedProductId",value = "成品入库单ID")
    @Excel(name = "成品入库单ID", height = 20, width = 30) 
    @Column(name = "finished_product_id")
    private String finishedProductId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30)
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Excel(name = "储位ID", height = 20, width = 30) 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 栈板编码
     */
    @ApiModelProperty(name="pallet_code",value = "栈板编码")
    @Excel(name = "栈板编码", height = 20, width = 30)
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 箱数
     */
    @ApiModelProperty(name="cartonQuantity",value = "箱数")
    @Excel(name = "箱数", height = 20, width = 30) 
    @Column(name = "carton_quantity")
    private BigDecimal cartonQuantity;

    /**
     * 计划入库数量
     */
    @ApiModelProperty(name="planInQuantity",value = "计划入库数量")
//    @Excel(name = "计划入库数量", height = 20, width = 30,orderNum="6")
    @Column(name = "plan_in_quantity")
    private BigDecimal planInQuantity;

    /**
     * 入库数量
     */
    @ApiModelProperty(name="inQuantity",value = "入库数量")
    @Excel(name = "入库数量", height = 20, width = 30) 
    @Column(name = "in_quantity")
    private BigDecimal inQuantity;

    /**
     * 入库时间
     */
    @ApiModelProperty(name="inTime",value = "入库时间")
    @Excel(name = "入库时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "in_time")
    private Date inTime;

    /**
     * 入库部门ID
     */
    @ApiModelProperty(name="deptId",value = "入库部门ID")
//    @Excel(name = "入库部门ID", height = 20, width = 30)
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 入库状态（0-未入库 1-部分入库 2-已完成）
     */
    @ApiModelProperty(name="inStatus",value = "入库状态（0-未入库 1-部分入库 2-已完成）")
    private Byte inStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30) 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30) 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30) 
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30) 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30) 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30) 
    @Column(name = "is_delete")
    private Byte isDelete;

    @Excel(name = "成品编码", height = 20, width = 30)
    @ApiModelProperty(name="productModelCode" ,value="成品编码")
    private String productModelCode;

    @Excel(name = "成品名称", height = 20, width = 30)
    @ApiModelProperty(name="productModelName" ,value="成品名称")
    private String productModelName;

    @Excel(name = "成品描述", height = 20, width = 30)
    @ApiModelProperty(name="productModelDesc" ,value="成品描述（规格？）")
    private String productModelDesc;

    @Excel(name = "仓库名称", height = 20, width = 30)
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    @Excel(name = "仓库区域名称", height = 20, width = 30)
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    @Excel(name = "储位名称", height = 20, width = 30)
    @ApiModelProperty(name="storageName" ,value="储位名称")
    private String storageName;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode" ,value="组织编码")
    @Excel(name = "组织编码", height = 20, width = 30)
    private String organizationCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 入库部门
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "入库部门")
    private String deptName;

    //还少一个计量单位

    private static final long serialVersionUID = 1L;
}