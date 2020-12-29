package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 成品出库单明细
 * wms_out_finished_product_det
 * @author hyc
 * @date 2020-12-23 15:52:03
 */
@Data
@Table(name = "wms_out_finished_product_det")
public class WmsOutFinishedProductDet extends ValidGroup implements Serializable {
    /**
     * 成品出库明细单ID
     */
    @ApiModelProperty(name="finishedProductDetId",value = "成品出库明细单ID")
    @Id
    @Column(name = "finished_product_det_id")
    @NotNull(groups = update.class,message = "成品出库明细单ID不能为空")
    private Long finishedProductDetId;

    /**
     * 成品出库单ID
     */
    @ApiModelProperty(name="finishedProductId",value = "成品出库单ID")
    @Excel(name = "成品出库单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "finished_product_id")
    private Long finishedProductId;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="productModelId",value = "产品ID")
    @Excel(name = "产品ID", height = 20, width = 30,orderNum="") 
    @Column(name = "product_model_id")
    private Long productModelId;

    /**
     * 实际出库数量
     */
    @ApiModelProperty(name="realityOutquantity",value = "实际出库数量")
    @Excel(name = "实际出库数量", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_outquantity")
    private BigDecimal realityOutquantity;

    /**
     * 仓库ID（出货仓库）
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID（出货仓库）")
    @Excel(name = "仓库ID（出货仓库）", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 仓库管理员ID
     */
    @ApiModelProperty(name="warehouseUserId",value = "仓库管理员ID")
    @Excel(name = "仓库管理员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_user_id")
    private Long warehouseUserId;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

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

    private static final long serialVersionUID = 1L;
}