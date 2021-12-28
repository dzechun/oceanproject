package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 盘点单明细条码表
 * wms_inner_stock_order_det_barcode
 * @author Dylan
 * @date 2021-12-28 15:03:35
 */
@Data
@Table(name = "wms_inner_stock_order_det_barcode")
public class WmsInnerStockOrderDetBarcode extends ValidGroup implements Serializable {
    /**
     * 盘点单明细条码表ID
     */
    @ApiModelProperty(name="stockOrderDetBarcodeId",value = "盘点单明细条码表ID")
    @Excel(name = "盘点单明细条码表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "stock_order_det_barcode_id")
    private Long stockOrderDetBarcodeId;

    /**
     * 盘点单明细ID
     */
    @ApiModelProperty(name="stockOrderDetId",value = "盘点单明细ID")
    @Excel(name = "盘点单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "stock_order_det_id")
    private Long stockOrderDetId;

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
     * 盘点结果(1-盘点、2-已盘点、3-盘盈、4-盘亏)
     */
    @ApiModelProperty(name="stockResult",value = "盘点结果(1-盘点、2-已盘点、3-盘盈、4-盘亏)")
    @Excel(name = "盘点结果(1-盘点、2-已盘点、3-盘盈、4-盘亏)", height = 20, width = 30,orderNum="") 
    @Column(name = "stock_result")
    private Byte stockResult;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}