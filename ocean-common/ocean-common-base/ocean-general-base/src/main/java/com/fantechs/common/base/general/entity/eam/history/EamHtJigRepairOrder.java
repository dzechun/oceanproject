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
 * 治具维修单履历表
 * eam_ht_jig_repair_order
 * @author admin
 * @date 2021-08-16 14:51:22
 */
@Data
@Table(name = "eam_ht_jig_repair_order")
public class EamHtJigRepairOrder extends ValidGroup implements Serializable {
    /**
     * 治具维修单履历ID
     */
    @ApiModelProperty(name="htJigRepairOrderId",value = "治具维修单履历ID")
    @Excel(name = "治具维修单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_repair_order_id")
    private Long htJigRepairOrderId;

    /**
     * 治具维修单ID
     */
    @ApiModelProperty(name="jigRepairOrderId",value = "治具维修单ID")
    @Excel(name = "治具维修单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_repair_order_id")
    private Long jigRepairOrderId;

    /**
     * 维修单号
     */
    @ApiModelProperty(name="jigRepairOrderCode",value = "维修单号")
    @Excel(name = "维修单号", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_repair_order_code")
    private String jigRepairOrderCode;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Excel(name = "治具ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Excel(name = "治具条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_barcode_id")
    private Long jigBarcodeId;

    /**
     * 治具条码
     */
    @ApiModelProperty(name="jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_barcode")
    private String jigBarcode;

    /**
     * 报修时间
     */
    @ApiModelProperty(name="requestForRepairTime",value = "报修时间")
    @Excel(name = "报修时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "request_for_repair_time")
    private Date requestForRepairTime;

    /**
     * 单据状态(1-待维修 2-维修中 3-已维修)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待维修 2-维修中 3-已维修)")
    @Excel(name = "单据状态(1-待维修 2-维修中 3-已维修)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 治具维修人员ID
     */
    @ApiModelProperty(name="repairUserId",value = "治具维修人员ID")
    @Excel(name = "治具维修人员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "repair_user_id")
    private Long repairUserId;

    /**
     * 维修时间
     */
    @ApiModelProperty(name="repairTime",value = "维修时间")
    @Excel(name = "维修时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "repair_time")
    private Date repairTime;

    /**
     * 不良责任ID
     */
    @ApiModelProperty(name="badnessDutyId",value = "不良责任ID")
    @Excel(name = "不良责任ID", height = 20, width = 30,orderNum="") 
    @Column(name = "badness_duty_id")
    private Long badnessDutyId;

    /**
     * 不良原因ID
     */
    @ApiModelProperty(name="badnessCauseId",value = "不良原因ID")
    @Excel(name = "不良原因ID", height = 20, width = 30,orderNum="") 
    @Column(name = "badness_cause_id")
    private Long badnessCauseId;

    /**
     * 报修备注
     */
    @ApiModelProperty(name="requestForRepairRemark",value = "报修备注")
    @Excel(name = "报修备注", height = 20, width = 30,orderNum="") 
    @Column(name = "request_for_repair_remark")
    private String requestForRepairRemark;

    /**
     * 维修备注
     */
    @ApiModelProperty(name="repairRemark",value = "维修备注")
    @Excel(name = "维修备注", height = 20, width = 30,orderNum="") 
    @Column(name = "repair_remark")
    private String repairRemark;

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
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="4")
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="4")
    private String jigName;

    /**
     * 治具描述
     */
    @Transient
    @ApiModelProperty(name = "jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="4")
    private String jigDesc;

    /**
     * 治具型号
     */
    @Transient
    @ApiModelProperty(name = "jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="4")
    private String jigModel;

    /**
     * 不良原因编码
     */
    @Transient
    @ApiModelProperty(name = "badnessCauseCode",value = "不良原因编码")
    @Excel(name = "不良原因编码", height = 20, width = 30,orderNum="4")
    private String badnessCauseCode;

    /**
     * 不良原因描述
     */
    @Transient
    @ApiModelProperty(name = "badnessCauseDesc",value = "不良原因描述")
    @Excel(name = "不良原因描述", height = 20, width = 30,orderNum="4")
    private String badnessCauseDesc;

    /**
     * 不良责任编码
     */
    @Transient
    @ApiModelProperty(name = "badnessDutyCode",value = "不良责任编码")
    @Excel(name = "不良责任编码", height = 20, width = 30,orderNum="4")
    private String badnessDutyCode;

    /**
     * 不良责任描述
     */
    @Transient
    @ApiModelProperty(name = "badnessDutyDesc",value = "不良责任描述")
    @Excel(name = "不良责任描述", height = 20, width = 30,orderNum="4")
    private String badnessDutyDesc;

    /**
     * 不良现象编码
     */
    @Transient
    @ApiModelProperty(name = "badnessPhenotypeCode",value = "不良现象编码")
    @Excel(name = "不良现象编码", height = 20, width = 30,orderNum="4")
    private String badnessPhenotypeCode;

    /**
     * 不良现象描述
     */
    @Transient
    @ApiModelProperty(name = "badnessPhenotypeDesc",value = "不良现象描述")
    @Excel(name = "不良现象描述", height = 20, width = 30,orderNum="4")
    private String badnessPhenotypeDesc;

    /**
     * 维修人员
     */
    @Transient
    @ApiModelProperty(name = "repairUserName",value = "维修人员")
    @Excel(name = "维修人员", height = 20, width = 30,orderNum="4")
    private String repairUserName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}