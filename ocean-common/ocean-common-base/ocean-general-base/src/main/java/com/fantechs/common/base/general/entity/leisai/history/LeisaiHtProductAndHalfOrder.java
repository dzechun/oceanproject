package com.fantechs.common.base.general.entity.leisai.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 成品和半成品对应表单查询履历ID
 * leisai_ht_product_and_half_order
 * @author 81947
 * @date 2021-10-27 11:52:57
 */
@Data
@Table(name = "leisai_ht_product_and_half_order")
public class LeisaiHtProductAndHalfOrder extends ValidGroup implements Serializable {
    /**
     * 成品和半成品表单履历ID
     */
    @ApiModelProperty(name="htProductAndHalfOrderId",value = "成品和半成品表单履历ID")
    @Excel(name = "成品和半成品表单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_product_and_half_order_id")
    private Long htProductAndHalfOrderId;

    /**
     * 成品和半成品表单ID
     */
    @ApiModelProperty(name="productAndHalfOrderId",value = "成品和半成品表单ID")
    @Excel(name = "成品和半成品表单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "product_and_half_order_id")
    private Long productAndHalfOrderId;

    /**
     * 单据号
     */
    @ApiModelProperty(name="productAndHalfOrderCode",value = "单据号")
    @Excel(name = "单据号", height = 20, width = 30,orderNum="") 
    @Column(name = "product_and_half_order_code")
    private String productAndHalfOrderCode;

    /**
     * 扫描SN
     */
    @ApiModelProperty(name="scanSn",value = "扫描SN")
    @Excel(name = "扫描SN", height = 20, width = 30,orderNum="") 
    @Column(name = "scan_sn")
    private String scanSn;

    /**
     * 产品SN
     */
    @ApiModelProperty(name="productSn",value = "产品SN")
    @Excel(name = "产品SN", height = 20, width = 30,orderNum="") 
    @Column(name = "product_sn")
    private String productSn;

    /**
     * 生产订单
     */
    @ApiModelProperty(name="workOrderCode",value = "生产订单")
    @Excel(name = "生产订单", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="productMaterialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="") 
    @Column(name = "product_material_code")
    private String productMaterialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="productMaterialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="") 
    @Column(name = "product_material_desc")
    private String productMaterialDesc;

    /**
     * 半成品SN
     */
    @ApiModelProperty(name="halfProductSn",value = "半成品SN")
    @Excel(name = "半成品SN", height = 20, width = 30,orderNum="") 
    @Column(name = "half_product_sn")
    private String halfProductSn;

    /**
     * 委外订单
     */
    @ApiModelProperty(name="outsourceOrderCode",value = "委外订单")
    @Excel(name = "委外订单", height = 20, width = 30,orderNum="") 
    @Column(name = "outsource_order_code")
    private String outsourceOrderCode;

    /**
     * IPQC检验日期
     */
    @ApiModelProperty(name="ipqcInspectionTime",value = "IPQC检验日期")
    @Excel(name = "IPQC检验日期", height = 20, width = 30,orderNum="") 
    @Column(name = "ipqc_inspection_time")
    private Date ipqcInspectionTime;

    /**
     * 半成品料号
     */
    @ApiModelProperty(name="halfProductMaterialCode",value = "半成品料号")
    @Excel(name = "半成品料号", height = 20, width = 30,orderNum="") 
    @Column(name = "half_product_material_code")
    private String halfProductMaterialCode;

    /**
     * 半成品描述
     */
    @ApiModelProperty(name="halfProductMaterialDesc",value = "半成品描述")
    @Excel(name = "半成品描述", height = 20, width = 30,orderNum="") 
    @Column(name = "half_product_material_desc")
    private String halfProductMaterialDesc;

    /**
     * 记录人
     */
    @ApiModelProperty(name="recorder",value = "记录人")
    @Excel(name = "记录人", height = 20, width = 30,orderNum="") 
    private String recorder;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="") 
    private BigDecimal qty;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}