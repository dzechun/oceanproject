package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 所有单据条码关系表
 * wms_inner_material_barcode_re_order
 * @author jbb
 * @date 2021-12-14 14:10:13
 */
@Data
@Table(name = "wms_inner_material_barcode_re_order")
public class WmsInnerMaterialBarcodeReOrder extends ValidGroup implements Serializable {
    /**
     * 所有单据条码关系表ID
     */
    @ApiModelProperty(name="materialBarcodeReOrderId",value = "所有单据条码关系表ID")
    @Excel(name = "所有单据条码关系表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "material_barcode_re_order_id")
    private Long materialBarcodeReOrderId;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据类型编码")
    @Excel(name = "单据类型编码", height = 20, width = 30,orderNum="")
    @Column(name = "order_type_code")
    private String orderTypeCode;

    /**
     * 单据编码
     */
    @ApiModelProperty(name="orderCode",value = "单据编码")
    @Excel(name = "单据编码", height = 20, width = 30,orderNum="")
    @Column(name = "order_code")
    private String orderCode;

    /**
     * 单据ID
     */
    @ApiModelProperty(name="orderId",value = "单据ID")
    @Excel(name = "单据ID", height = 20, width = 30,orderNum="")
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 单据明细ID
     */
    @ApiModelProperty(name="orderDetId",value = "单据明细ID")
    @Excel(name = "单据明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "order_det_id")
    private Long orderDetId;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    @Excel(name = "来料条码ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_barcode_id")
    private Long materialBarcodeId;

    /**
     * 扫描状态(1-未扫描 2-已保存 3-已提交)
     */
    @ApiModelProperty(name="scanStatus",value = "扫描状态(1-未扫描 2-已保存 3-已提交)")
    @Excel(name = "扫描状态(1-未扫描 2-已保存 3-已提交)", height = 20, width = 30,orderNum="")
    @Column(name = "scan_status")
    private Byte scanStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="")
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
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long orgId;

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
