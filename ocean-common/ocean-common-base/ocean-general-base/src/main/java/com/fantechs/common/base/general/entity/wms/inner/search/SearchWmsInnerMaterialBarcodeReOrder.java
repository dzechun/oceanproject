package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class SearchWmsInnerMaterialBarcodeReOrder extends BaseQuery implements Serializable {

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据类型编码")
    private String orderTypeCode;

    /**
     * 单据ID
     */
    @ApiModelProperty(name="orderId",value = "单据ID")
    private Long orderId;

    /**
     * 单据明细ID
     */
    @ApiModelProperty(name="orderDetId",value = "单据明细ID")
    private Long orderDetId;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    private Long materialBarcodeId;

    /**
     * 扫描状态(1-未扫描 2-已保存 3-已提交)
     */
    @ApiModelProperty(name="scanStatus",value = "扫描状态(1-未扫描 2-已保存 3-已提交)")
    private Byte scanStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
