package com.fantechs.common.base.general.entity.wms.in;

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
import java.util.List;

;

/**
 * 其他入库单明细
 * wms_in_otherin_det
 * @author hyc
 * @date 2021-01-12 15:27:39
 */
@Data
@Table(name = "wms_in_otherin_det")
public class WmsInOtherinDet extends ValidGroup implements Serializable {

    /**
     * 其他入库单明细ID
     */
    @ApiModelProperty(name="otherinDetId",value = "其他入库单明细ID")
    @NotNull(groups = update.class,message = "其他入库单明细ID")
    @Id
    @Column(name = "otherin_det_id")
    private Long otherinDetId;

    /**
     * 其他入库单ID
     */
    @ApiModelProperty(name="otherinId",value = "其他入库单ID")
    @Excel(name = "其他入库单ID", height = 20, width = 30,orderNum="")
    @Column(name = "otherin_id")
    private String otherinId;

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
    @ApiModelProperty(name="palletCode",value = "栈板编码")
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

    private static final long serialVersionUID = 1L;
}