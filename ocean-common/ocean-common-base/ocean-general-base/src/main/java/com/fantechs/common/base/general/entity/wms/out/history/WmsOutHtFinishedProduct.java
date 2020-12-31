package com.fantechs.common.base.general.entity.wms.out.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 成品出库单
 * wms_out_ht_finished_product
 * @author hyc
 * @date 2020-12-31 09:54:50
 */
@Data
@Table(name = "wms_out_ht_finished_product")
public class WmsOutHtFinishedProduct implements Serializable {
    @Id
    @Column(name = "ht_finished_product_id")
    private Long htFinishedProductId;

    /**
     * 成品出库单ID
     */
    @ApiModelProperty(name="finishedProductId",value = "成品出库单ID")
    @Excel(name = "成品出库单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "finished_product_id")
    private Long finishedProductId;

    /**
     * 成品出库单号
     */
    @ApiModelProperty(name="finishedProductCode",value = "成品出库单号")
    @Excel(name = "成品出库单号", height = 20, width = 30,orderNum="") 
    @Column(name = "finished_product_code")
    private String finishedProductCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="customerId",value = "客户ID")
    @Excel(name = "客户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 预计出库时间
     */
    @ApiModelProperty(name="planOuttime",value = "预计出库时间")
    @Excel(name = "预计出库时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_outtime")
    private Date planOuttime;

    /**
     * 实际出库时间
     */
    @ApiModelProperty(name="realityOuttime",value = "实际出库时间")
    @Excel(name = "实际出库时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "reality_outtime")
    private Date realityOuttime;

    /**
     * 计划出库数量
     */
    @ApiModelProperty(name="planOutquantity",value = "计划出库数量")
    @Excel(name = "计划出库数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_outquantity")
    private BigDecimal planOutquantity;

    /**
     * 实际出库数量
     */
    @ApiModelProperty(name="realityOutquantity",value = "实际出库数量")
    @Excel(name = "实际出库数量", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_outquantity")
    private BigDecimal realityOutquantity;

    /**
     * 运输方式（0-陆运 1-海运 2-空运
     */
    @ApiModelProperty(name="trafficType",value = "运输方式（0-陆运 1-海运 2-空运")
    @Excel(name = "运输方式（0-陆运 1-海运 2-空运", height = 20, width = 30,orderNum="") 
    @Column(name = "traffic_type")
    private Byte trafficType;

    /**
     * 单据状态（0-待出库 1-出库中 2-出库完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待出库 1-出库中 2-出库完成）")
    @Excel(name = "单据状态（0-待出库 1-出库中 2-出库完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

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
     * 客户代码
     */
    @ApiModelProperty(name="supplierCode" ,value="客户代码")
    private String supplierCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="supplierName" ,value="客户名称")
    private String supplierName;
}