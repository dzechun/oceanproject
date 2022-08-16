package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 车间管理-返工单条码履历表
 * mes_sfc_ht_rework_order_barcode
 * @author bgkun
 * @date 2021-06-15 11:08:54
 */
@Data
@Table(name = "mes_sfc_ht_rework_order_barcode")
public class MesSfcHtReworkOrderBarcode extends ValidGroup implements Serializable {
    /**
     * 返工单条码履历ID
     */
    @ApiModelProperty(name="htReworkOrderBarcodeId",value = "返工单条码履历ID")
    @Excel(name = "返工单条码履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_rework_order_barcode_id")
    private Long htReworkOrderBarcodeId;

    /**
     * 返工单条码ID
     */
    @ApiModelProperty(name="reworkOrderBarcodeId",value = "返工单条码ID")
    @Excel(name = "返工单条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "rework_order_barcode_id")
    private Long reworkOrderBarcodeId;

    /**
     * 返工单ID
     */
    @ApiModelProperty(name="reworkOrderId",value = "返工单ID")
    @Excel(name = "返工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "rework_order_id")
    private Long reworkOrderId;

    /**
     * 工单条码ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "工单条码ID")
    @Excel(name = "工单条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 返工工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "返工工单ID")
    @Excel(name = "返工工单ID", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_id")
    private Long workOrderId;

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