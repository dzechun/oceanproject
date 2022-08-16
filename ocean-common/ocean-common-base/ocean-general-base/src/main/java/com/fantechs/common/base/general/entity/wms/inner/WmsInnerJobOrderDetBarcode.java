package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上架单明细条码表
 * wms_inner_job_order_det_barcode
 * @author mr.lei
 * @date 2021-05-07 11:45:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wms_inner_job_order_det_barcode")
public class WmsInnerJobOrderDetBarcode extends ValidGroup implements Serializable {
    /**
     * 上架单明细条码表ID
     */
    @ApiModelProperty(name="jobOrderDetBarcodeId",value = "上架单明细条码表ID")
    @Excel(name = "上架单明细条码表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "job_order_det_barcode_id")
    private Long jobOrderDetBarcodeId;

    /**
     * 上架单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "上架单明细ID")
    @Excel(name = "上架单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "job_order_det_id")
    private Long jobOrderDetId;

    /**
     * 生产订单条码ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "生产订单条码ID")
    @Excel(name = "生产订单条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="") 
    private String barcode;

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



    /**
     *  ===============================
     * 2022-03-24
     * 万宝项目
     * 增加上架作业单跟条码关系
     * 因为堆垛需要在上架作业提交之前释放，导致上架作业单找不到条码，故，有此改动
     */

    /**
     * 客户条码
     */
    @ApiModelProperty(name="customerBarcode",value = "客户条码")
    @Excel(name = "客户条码", height = 20, width = 30,orderNum="")
    @Column(name = "customer_barcode")
    private String customerBarcode;

    /**
     * 销售条码
     */
    @ApiModelProperty(name="salesBarcode",value = "销售条码")
    @Excel(name = "销售条码", height = 20, width = 30,orderNum="")
    @Column(name = "sales_barcode")
    private String salesBarcode;

    // ===============================
}