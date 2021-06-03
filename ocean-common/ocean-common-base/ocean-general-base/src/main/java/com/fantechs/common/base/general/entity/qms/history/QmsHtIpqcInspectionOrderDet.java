package com.fantechs.common.base.general.entity.qms.history;

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
 * IPQC检验单明细履历表
 * qms_ht_ipqc_inspection_order_det
 * @author admin
 * @date 2021-06-02 14:03:33
 */
@Data
@Table(name = "qms_ht_ipqc_inspection_order_det")
public class QmsHtIpqcInspectionOrderDet extends ValidGroup implements Serializable {
    /**
     * IPQC检验单明细履历ID
     */
    @ApiModelProperty(name="htIpqcInspectionOrderDetId",value = "IPQC检验单明细履历ID")
    @Excel(name = "IPQC检验单明细履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_ipqc_inspection_order_det_id")
    private Long htIpqcInspectionOrderDetId;

    /**
     * IPQC检验单明细ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderDetId",value = "IPQC检验单明细ID")
    @Excel(name = "IPQC检验单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "ipqc_inspection_order_det_id")
    private Long ipqcInspectionOrderDetId;

    /**
     * IPQC检验单ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderId",value = "IPQC检验单ID")
    @Excel(name = "IPQC检验单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "ipqc_inspection_order_id")
    private Long ipqcInspectionOrderId;

    /**
     * 过程检验项目检验项ID
     */
    @ApiModelProperty(name="processInspectionItemItemId",value = "过程检验项目检验项ID")
    @Excel(name = "过程检验项目检验项ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_inspection_item_item_id")
    private Long processInspectionItemItemId;

    /**
     * 检验结果
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果")
    @Excel(name = "检验结果", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_result")
    private String inspectionResult;

    /**
     * 检验时间
     */
    @ApiModelProperty(name="inspectionTime",value = "检验时间")
    @Excel(name = "检验时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "inspection_time")
    private Date inspectionTime;

    /**
     * 问题记录
     */
    @ApiModelProperty(name="problemRecord",value = "问题记录")
    @Excel(name = "问题记录", height = 20, width = 30,orderNum="") 
    @Column(name = "problem_record")
    private String problemRecord;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="11")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="13")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}