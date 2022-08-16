package com.fantechs.common.base.general.entity.eam.history;

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
 * 治具报废单明细履历表
 * eam_ht_jig_scrap_order_det
 * @author admin
 * @date 2021-08-19 13:42:41
 */
@Data
@Table(name = "eam_ht_jig_scrap_order_det")
public class EamHtJigScrapOrderDet extends ValidGroup implements Serializable {
    /**
     * 治具报废单明细履历ID
     */
    @ApiModelProperty(name="htJigScrapOrderDetId",value = "治具报废单明细履历ID")
    @Excel(name = "治具报废单明细履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_scrap_order_det_id")
    private Long htJigScrapOrderDetId;

    /**
     * 治具报废单明细ID
     */
    @ApiModelProperty(name="jigScrapOrderDetId",value = "治具报废单明细ID")
    @Excel(name = "治具报废单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_scrap_order_det_id")
    private Long jigScrapOrderDetId;

    /**
     * 治具报废单ID
     */
    @ApiModelProperty(name="jigScrapOrderId",value = "治具报废单ID")
    @Excel(name = "治具报废单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_scrap_order_id")
    private Long jigScrapOrderId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Excel(name = "治具条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_barcode_id")
    private Long jigBarcodeId;

    /**
     * 限购金额
     */
    @ApiModelProperty(name="purchaseLimitationAmount",value = "限购金额")
    @Excel(name = "限购金额", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_limitation_amount")
    private String purchaseLimitationAmount;

    /**
     * 资产净值
     */
    @ApiModelProperty(name="assetNetValue",value = "资产净值")
    @Excel(name = "资产净值", height = 20, width = 30,orderNum="") 
    @Column(name = "asset_net_value")
    private String assetNetValue;

    /**
     * 估计废品金额
     */
    @ApiModelProperty(name="estimateScrapAmount",value = "估计废品金额")
    @Excel(name = "估计废品金额", height = 20, width = 30,orderNum="") 
    @Column(name = "estimate_scrap_amount")
    private String estimateScrapAmount;

    /**
     * 已提折旧
     */
    @ApiModelProperty(name="alreadyDepreciation",value = "已提折旧")
    @Excel(name = "已提折旧", height = 20, width = 30,orderNum="") 
    @Column(name = "already_depreciation")
    private String alreadyDepreciation;

    /**
     * 小计汇总
     */
    @ApiModelProperty(name="subtotalSummary",value = "小计汇总")
    @Excel(name = "小计汇总", height = 20, width = 30,orderNum="") 
    @Column(name = "subtotal_summary")
    private String subtotalSummary;

    /**
     * 报废说明
     */
    @ApiModelProperty(name="scrapDescription",value = "报废说明")
    @Excel(name = "报废说明", height = 20, width = 30,orderNum="") 
    @Column(name = "scrap_description")
    private String scrapDescription;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 治具条码
     */
    @Transient
    @ApiModelProperty(name = "jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="6")
    private String jigBarcode;

    /**
     * 资产条码
     */
    @Transient
    @ApiModelProperty(name = "assetCode",value = "资产条码")
    @Excel(name = "资产条码", height = 20, width = 30,orderNum="6")
    private String assetCode;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="6")
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="6")
    private String jigName;

    /**
     * 治具描述
     */
    @Transient
    @ApiModelProperty(name = "jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="6")
    private String jigDesc;

    /**
     * 治具型号
     */
    @Transient
    @ApiModelProperty(name = "jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="6")
    private String jigModel;

    /**
     * 财产编码类别(1-固定资产  2-列管品)
     */
    @Transient
    @ApiModelProperty(name = "propertyCodeCategory",value = "财产编码类别(1-固定资产  2-列管品)")
    @Excel(name = "财产编码类别(1-固定资产  2-列管品)", height = 20, width = 30,orderNum="6")
    private Byte propertyCodeCategory;

    /**
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="6")
    private String jigCategoryName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}