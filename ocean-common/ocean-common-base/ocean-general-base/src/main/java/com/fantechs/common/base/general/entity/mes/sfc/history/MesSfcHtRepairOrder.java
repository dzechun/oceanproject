package com.fantechs.common.base.general.entity.mes.sfc.history;

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
 * 车间管理-维修单履历表
 * mes_sfc_ht_repair_order
 * @author admin
 * @date 2021-09-10 11:18:50
 */
@Data
@Table(name = "mes_sfc_ht_repair_order")
public class MesSfcHtRepairOrder extends ValidGroup implements Serializable {
    /**
     * 维修单履历ID
     */
    @ApiModelProperty(name="htRepairOrderId",value = "维修单履历ID")
    @Excel(name = "维修单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_repair_order_id")
    private Long htRepairOrderId;

    /**
     * 维修单ID
     */
    @ApiModelProperty(name="repairOrderId",value = "维修单ID")
    @Excel(name = "维修单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "repair_order_id")
    private Long repairOrderId;

    /**
     * 维修单号
     */
    @ApiModelProperty(name="repairOrderCode",value = "维修单号")
    @Excel(name = "维修单号", height = 20, width = 30,orderNum="") 
    @Column(name = "repair_order_code")
    private String repairOrderCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 生产订单条码ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "生产订单条码ID")
    @Excel(name = "生产订单条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 生产订单条码
     */
    @ApiModelProperty(name="barcode",value = "生产订单条码")
    @Excel(name = "生产订单条码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 半成品条码
     */
    @ApiModelProperty(name="semiProductBarcode",value = "半成品条码")
    @Excel(name = "半成品条码", height = 20, width = 30,orderNum="") 
    @Column(name = "semi_product_barcode")
    private String semiProductBarcode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Excel(name = "产线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    @Excel(name = "工艺路线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 不良工序ID
     */
    @ApiModelProperty(name="badProcessId",value = "不良工序ID")
    @Excel(name = "不良工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "bad_process_id")
    private Long badProcessId;

    /**
     * 当前工序ID
     */
    @ApiModelProperty(name="currentProcessId",value = "当前工序ID")
    @Excel(name = "当前工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "current_process_id")
    private Long currentProcessId;

    /**
     * 单据状态(1、待维修 2、已维修)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1、待维修 2、已维修)")
    @Excel(name = "单据状态(1、待维修 2、已维修)", height = 20, width = 30,orderNum="")
    @Column(name = "order_status")
    private Byte orderStatus;

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

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="11")
    private String workOrderCode;

    /**
     * 生产订单条码
     */
    @Transient
    @ApiModelProperty(name = "workOrderBarcode",value = "生产订单条码")
    @Excel(name = "生产订单条码", height = 20, width = 30,orderNum="11")
    private String workOrderBarcode;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="11")
    private String materialCode;


    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="11")
    private String materialDesc;

    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(name = "proName",value = "产线名称")
    @Excel(name = "产线名称", height = 20, width = 30,orderNum="11")
    private String proName;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name = "routeName",value = "工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="11")
    private String routeName;

    /**
     * 不良工序名称
     */
    @Transient
    @ApiModelProperty(name = "badProcessName",value = "不良工序名称")
    @Excel(name = "不良工序名称", height = 20, width = 30,orderNum="11")
    private String badProcessName;

    /**
     * 当前工序名称
     */
    @Transient
    @ApiModelProperty(name = "currentProcessName",value = "当前工序名称")
    @Excel(name = "当前工序名称", height = 20, width = 30,orderNum="11")
    private String currentProcessName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}