package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 治具报废单明细
 * eam_jig_scrap_order_det
 * @author admin
 * @date 2021-08-19 13:42:41
 */
@Data
@Table(name = "eam_jig_scrap_order_det")
public class EamJigScrapOrderDet extends ValidGroup implements Serializable {
    /**
     * 治具报废单明细ID
     */
    @ApiModelProperty(name="jigScrapOrderDetId",value = "治具报废单明细ID")
    @Excel(name = "治具报废单明细ID", height = 20, width = 30,orderNum="") 
    @Id
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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}