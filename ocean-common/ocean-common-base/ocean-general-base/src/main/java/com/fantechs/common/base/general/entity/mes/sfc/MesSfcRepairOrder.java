package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 车间管理-维修单
 * mes_sfc_repair_order
 * @author admin
 * @date 2021-09-10 11:18:50
 */
@Data
@Table(name = "mes_sfc_repair_order")
public class MesSfcRepairOrder extends ValidGroup implements Serializable {
    /**
     * 维修单ID
     */
    @ApiModelProperty(name="repairOrderId",value = "维修单ID")
    @Id
    @Column(name = "repair_order_id")
    private Long repairOrderId;

    /**
     * 维修单号
     */
    @ApiModelProperty(name="repairOrderCode",value = "维修单号")
    @Excel(name = "维修单号", height = 20, width = 30,orderNum="1")
    @Column(name = "repair_order_code")
    private String repairOrderCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 生产订单条码ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "生产订单条码ID")
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 生产订单条码
     */
    @ApiModelProperty(name="barcode",value = "生产订单条码")
    private String barcode;

    /**
     * 半成品条码
     */
    @ApiModelProperty(name="semiProductBarcode",value = "半成品条码")
    @Excel(name = "半成品条码", height = 20, width = 30,orderNum="3")
    @Column(name = "semi_product_barcode")
    private String semiProductBarcode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 不良工序ID
     */
    @ApiModelProperty(name="badProcessId",value = "不良工序ID")
    @Column(name = "bad_process_id")
    private Long badProcessId;

    /**
     * 当前工序ID
     */
    @ApiModelProperty(name="currentProcessId",value = "当前工序ID")
    @Column(name = "current_process_id")
    private Long currentProcessId;

    /**
     * 转接确认状态(1-未确认 2-已确认)
     */
    @ApiModelProperty(name="transferComfirmStatus",value = "转接确认状态(1-未确认 2-已确认)")
    //@Excel(name = "转接确认状态(1-未确认 2-已确认)", height = 20, width = 30,orderNum="10")
    @Column(name = "transfer_comfirm_status")
    private Byte transferComfirmStatus;

    /**
     * 转接确认人ID
     */
    @ApiModelProperty(name="transferComfirmUserId",value = "转接确认人ID")
    @Column(name = "transfer_comfirm_user_id")
    private Long transferComfirmUserId;

    /**
     * 转接确认时间
     */
    @ApiModelProperty(name="transferComfirmTime",value = "转接确认时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "transfer_comfirm_time")
    private Date transferComfirmTime;

    /**
     * 转出确认状态(1-未确认 2-已确认)
     */
    @ApiModelProperty(name="transferOutComfirmStatus",value = "转出确认状态(1-未确认 2-已确认)")
    //9@Excel(name = "转接确认状态(1-未确认 2-已确认)", height = 20, width = 30,orderNum="10")
    @Column(name = "transfer_out_comfirm_status")
    private Byte transferOutComfirmStatus;

    /**
     * 转出确认人ID
     */
    @ApiModelProperty(name="transferOutComfirmUserId",value = "转出确认人ID")
    @Column(name = "transfer_out_comfirm_user_id")
    private Long transferOutComfirmUserId;

    /**
     * 转出确认时间
     */
    @ApiModelProperty(name="transferOutComfirmTime",value = "转出确认时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "transfer_out_comfirm_time")
    private Date transferOutComfirmTime;

    /**
     * 单据状态(1、待维修 2、已维修)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1、待维修 2、已维修)")
    @Excel(name = "单据状态(1、待维修 2、已维修)", height = 20, width = 30,orderNum="5")
    @Column(name = "order_status")
    private Byte orderStatus;

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
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 维修单不良现象列表
     */
    @ApiModelProperty(name="mesSfcRepairOrderBadPhenotypeList",value = "维修单不良现象列表")
    private List<MesSfcRepairOrderBadPhenotype>  mesSfcRepairOrderBadPhenotypeList = new ArrayList<>();

    /**
     * 维修单不良现象维修列表
     */
    @ApiModelProperty(name="mesSfcRepairOrderBadPhenotypeRepairList",value = "维修单不良现象维修列表")
    private List<MesSfcRepairOrderBadPhenotypeRepair> mesSfcRepairOrderBadPhenotypeRepairList = new ArrayList<>();

    /**
     * 维修单半成品列表
     */
    @ApiModelProperty(name="mesSfcRepairOrderSemiProductList",value = "维修单半成品列表")
    private List<MesSfcRepairOrderSemiProduct>  mesSfcRepairOrderSemiProductList = new ArrayList<>();

    /**
     * 文件信息列表
     */
    @ApiModelProperty(name="baseFileList",value = "文件信息列表")
    private List<BaseFile>  baseFileList = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}