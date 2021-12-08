package com.fantechs.common.base.general.entity.qms;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 不良品处理
 * qms_badness_manage
 * @author admin
 * @date 2021-12-08 14:00:59
 */
@Data
@Table(name = "qms_badness_manage")
public class QmsBadnessManage extends ValidGroup implements Serializable {
    /**
     * 不良品处理ID
     */
    @ApiModelProperty(name="badnessManageId",value = "不良品处理ID")
    @Id
    @Column(name = "badness_manage_id")
    private Long badnessManageId;

    /**
     * 来料检验单ID
     */
    @ApiModelProperty(name="incomingInspectionOrderId",value = "来料检验单ID")
    @Column(name = "incoming_inspection_order_id")
    private Long incomingInspectionOrderId;

    /**
     * 特采数量
     */
    @ApiModelProperty(name="specialReceiveQty",value = "特采数量")
    @Excel(name = "特采数量", height = 20, width = 30,orderNum="9")
    @Column(name = "special_receive_qty")
    private BigDecimal specialReceiveQty;

    /**
     * 退货数量
     */
    @ApiModelProperty(name="returnQty",value = "退货数量")
    @Excel(name = "退货数量", height = 20, width = 30,orderNum="10")
    @Column(name = "return_qty")
    private BigDecimal returnQty;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(name="operatorUserId",value = "操作用户ID")
    @Column(name = "operator_user_id")
    private Long operatorUserId;

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
     * 来料检验单号
     */
    @ApiModelProperty(name="incomingInspectionOrderCode",value = "来料检验单号")
    @Excel(name = "来料检验单号", height = 20, width = 30,orderNum="1")
    @Transient
    private String incomingInspectionOrderCode;

    /**
     * 操作人
     */
    @ApiModelProperty(name="operatorUserName",value = "操作人")
    @Transient
    private String operatorUserName;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="2")
    @Transient
    private String materialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="3")
    @Transient
    private String materialDesc;

    /**
     * 产品版本
     */
    @ApiModelProperty(name="materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="4")
    @Transient
    private String materialVersion;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="5")
    @Transient
    private String productModelName;

    /**
     * 供应商
     */
    @ApiModelProperty(name="supplierName",value = "供应商")
    @Excel(name = "供应商", height = 20, width = 30,orderNum="6")
    @Transient
    private String supplierName;

    /**
     * 单据数量
     */
    @ApiModelProperty(name="orderQty",value = "单据数量")
    @Excel(name = "单据数量", height = 20, width = 30,orderNum="8")
    @Transient
    private BigDecimal orderQty;

    /**
     * MRB评审(1-特采 2-挑选使用 3-退供应商)
     */
    @ApiModelProperty(name="mrbResult",value = "MRB评审(1-特采 2-挑选使用 3-退供应商)")
    @Excel(name = "MRB评审(1-特采 2-挑选使用 3-退供应商)", height = 20, width = 30,orderNum="7")
    @Transient
    private Byte mrbResult;

    /**
     * 条码列表
     */
    @ApiModelProperty(name="barcodeList",value = "条码列表")
    private List<QmsBadnessManageBarcode> barcodeList = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}