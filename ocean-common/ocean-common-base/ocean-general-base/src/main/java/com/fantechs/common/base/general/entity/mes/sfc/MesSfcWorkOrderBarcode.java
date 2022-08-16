package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.Builder;
import lombok.Data;

/**
 * 生产管理-生产订单条码表
 * mes_sfc_work_order_barcode
 * @author mr.lei
 * @date 2021-04-07 11:36:12
 */
@Data
@Table(name = "mes_sfc_work_order_barcode")
public class MesSfcWorkOrderBarcode extends ValidGroup implements Serializable {
    /**
     * 生产订单条码ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "生产订单条码ID")
    @Excel(name = "生产订单条码ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 父ID
     */
    @ApiModelProperty(name="parentId",value = "父ID")
    @Excel(name = "父ID", height = 20, width = 30,orderNum="") 
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工单编码
     */
    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    @Excel(name = "工单编码", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
     */
    @ApiModelProperty(name="barcodeStatus",value = "条码状态(0-待投产 1-投产中 2-已完成 3-待打印)")
    @Excel(name = "条码状态(0-待投产 1-投产中 2-已完成 3-待打印)", height = 20, width = 30,orderNum="")
    @Column(name = "barcode_status")
    private Byte barcodeStatus;

    /**
     * 标签类别信息ID
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别信息ID")
    @Excel(name = "标签类别信息ID", height = 20, width = 30,orderNum="")
    @Column(name = "label_category_id")
    private Long labelCategoryId;


    @Transient
    private Byte barcodeType;

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
     * 打印时间
     */
    @ApiModelProperty(name="printTime",value = "打印时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "print_time")
    private Date printTime;

    /**
     * 生成时间
     */
    @ApiModelProperty(name="createBarcodeTime",value = "生成时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_barcode_time")
    private Date createBarcodeTime;

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

    private LabelRuteDto labelRuteDto;

    /**
     * 产生数量
     */
    @Transient
    @ApiModelProperty(name = "qty",value = "打印数量")
    private Integer qty;

    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name = "workOrderQty",value = "工单数量")
    private BigDecimal workOrderQty;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name= "materialCode", value = "产品料号")
    private String materialCode;

    @Transient
    private Long salesOrderDetId;

    @Transient
    private BigDecimal orderQty;

    @Transient
    private String salesOrderCode;

    @Transient
    private Long salesOrderId;

    private static final long serialVersionUID = 1L;
}