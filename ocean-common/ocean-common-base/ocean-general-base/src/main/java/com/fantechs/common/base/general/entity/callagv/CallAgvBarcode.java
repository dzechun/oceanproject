package com.fantechs.common.base.general.entity.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 条码表
 * call_agv_barcode
 * @author 86178
 * @date 2021-09-15 16:31:56
 */
@Data
@Table(name = "call_agv_barcode")
public class CallAgvBarcode extends ValidGroup implements Serializable {
    /**
     * 条码表ID
     */
    @ApiModelProperty(name="barcodeId",value = "条码表ID")
    @Excel(name = "条码表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "barcode_id")
    private Long barcodeId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 型号
     */
    @ApiModelProperty(name="productModel",value = "型号")
    @Excel(name = "型号", height = 20, width = 30,orderNum="") 
    @Column(name = "product_model")
    private String productModel;

    /**
     * 批次
     */
    @ApiModelProperty(name="batch",value = "批次")
    @Excel(name = "批次", height = 20, width = 30,orderNum="") 
    private String batch;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    @Excel(name = "批号", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="") 
    private BigDecimal qty;

    /**
     * 作业员
     */
    @ApiModelProperty(name="operatorUserName",value = "作业员")
    @Excel(name = "作业员", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_user_name")
    private String operatorUserName;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="") 
    @Column(name = "process_name")
    private String processName;

    /**
     * 保质期
     */
    @ApiModelProperty(name="shelfLife",value = "保质期")
    @Excel(name = "保质期", height = 20, width = 30,orderNum="") 
    @Column(name = "shelf_life")
    private String shelfLife;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="") 
    @Column(name = "production_date")
    private String productionDate;

    /**
     * 流水号
     */
    @ApiModelProperty(name="serialNumber",value = "流水号")
    @Excel(name = "流水号", height = 20, width = 30,orderNum="") 
    @Column(name = "serial_number")
    private String serialNumber;

    /**
     * 注塑机名称
     */
    @ApiModelProperty(name="zhuSuJiName",value = "注塑机名称")
    @Excel(name = "注塑机名称", height = 20, width = 30,orderNum="") 
    @Column(name = "zhu_su_ji_name")
    private String zhuSuJiName;

    /**
     * 注塑机班次
     */
    @ApiModelProperty(name="zhuSuJiShift",value = "注塑机班次")
    @Excel(name = "注塑机班次", height = 20, width = 30,orderNum="") 
    @Column(name = "zhu_su_ji_shift")
    private String zhuSuJiShift;

    /**
     * 折弯机名称
     */
    @ApiModelProperty(name="zheWanJiName",value = "折弯机名称")
    @Excel(name = "折弯机名称", height = 20, width = 30,orderNum="") 
    @Column(name = "zhe_wan_ji_name")
    private String zheWanJiName;

    /**
     * 折弯机班次
     */
    @ApiModelProperty(name="zheWanJiShift",value = "折弯机班次")
    @Excel(name = "折弯机班次", height = 20, width = 30,orderNum="") 
    @Column(name = "zhe_wan_ji_shift")
    private String zheWanJiShift;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
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