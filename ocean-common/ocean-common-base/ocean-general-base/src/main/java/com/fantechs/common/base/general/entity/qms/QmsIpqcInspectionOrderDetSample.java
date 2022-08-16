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
 * IPQC检验单明细样本值表
 * qms_ipqc_inspection_order_det_sample
 * @author admin
 * @date 2021-06-04 15:43:36
 */
@Data
@Table(name = "qms_ipqc_inspection_order_det_sample")
public class QmsIpqcInspectionOrderDetSample extends ValidGroup implements Serializable {
    /**
     * 检验单明细样本值ID
     */
    @ApiModelProperty(name="inspectionOrderDetSampleId",value = "检验单明细样本值ID")
    @Excel(name = "检验单明细样本值ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "inspection_order_det_sample_id")
    private Long inspectionOrderDetSampleId;

    /**
     * IPQC检验单明细ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderDetId",value = "IPQC检验单明细ID")
    @Excel(name = "IPQC检验单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "ipqc_inspection_order_det_id")
    private Long ipqcInspectionOrderDetId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 样本值
     */
    @ApiModelProperty(name="sampleValue",value = "样本值")
    @Excel(name = "样本值", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_value")
    private String sampleValue;

    /**
     * 不良现象ID
     */
    @ApiModelProperty(name="badnessPhenotypeId",value = "不良现象ID")
    @Excel(name = "不良现象ID", height = 20, width = 30,orderNum="") 
    @Column(name = "badness_phenotype_id")
    private Long badnessPhenotypeId;

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
     * 不良现象代码
     */
    @Transient
    @ApiModelProperty(name = "badnessPhenotypeCode",value = "不良现象代码")
    @Excel(name = "不良现象代码", height = 20, width = 30,orderNum="13")
    private String badnessPhenotypeCode;

    /**
     * 不良现象描述
     */
    @Transient
    @ApiModelProperty(name = "badnessPhenotypeDesc",value = "不良现象描述")
    @Excel(name = "不良现象描述", height = 20, width = 30,orderNum="13")
    private String badnessPhenotypeDesc;

    /**
     * 不良类别id
     */
    @Transient
    @ApiModelProperty(name = "badnessCategoryId",value = "不良类别id")
    private Long badnessCategoryId;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}