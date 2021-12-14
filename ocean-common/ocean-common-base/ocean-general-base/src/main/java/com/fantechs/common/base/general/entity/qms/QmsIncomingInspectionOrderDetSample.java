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
import java.util.Date;

;
;

/**
 * 来料检验单明细样本值表
 * qms_incoming_inspection_order_det_sample
 * @author admin
 * @date 2021-12-06 10:30:34
 */
@Data
@Table(name = "qms_incoming_inspection_order_det_sample")
public class QmsIncomingInspectionOrderDetSample extends ValidGroup implements Serializable {
    /**
     * 来料检验单明细样本值ID
     */
    @ApiModelProperty(name="incomingInspectionOrderDetSampleId",value = "来料检验单明细样本值ID")
    @Id
    @Column(name = "incoming_inspection_order_det_sample_id")
    private Long incomingInspectionOrderDetSampleId;

    /**
     * 来料检验单明细ID
     */
    @ApiModelProperty(name="incomingInspectionOrderDetId",value = "来料检验单明细ID")
    @Column(name = "incoming_inspection_order_det_id")
    private Long incomingInspectionOrderDetId;

    /**
     * 来料检验单ID
     */
    @ApiModelProperty(name="incomingInspectionOrderId",value = "来料检验单ID")
    @Column(name = "incoming_inspection_order_id")
    private Long incomingInspectionOrderId;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    @Column(name = "material_barcode_id")
    private Long materialBarcodeId;

    /**
     * 条码
     */
    @Transient
    @ApiModelProperty(name = "barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="1")
    private String barcode;

    /**
     * 样本值
     */
    @ApiModelProperty(name="sampleValue",value = "样本值")
    @Excel(name = "样本值", height = 20, width = 30,orderNum="2")
    @Column(name = "sample_value")
    private String sampleValue;

    /**
     * 不良现象ID
     */
    @ApiModelProperty(name="badnessPhenotypeId",value = "不良现象ID")
    @Column(name = "badness_phenotype_id")
    private Long badnessPhenotypeId;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}