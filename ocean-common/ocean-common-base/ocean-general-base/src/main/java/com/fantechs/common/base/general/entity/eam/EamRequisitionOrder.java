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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 设备领用单
 * eam_requisition_order
 * @author admin
 * @date 2021-06-29 10:35:09
 */
@Data
@Table(name = "eam_requisition_order")
public class EamRequisitionOrder extends ValidGroup implements Serializable {
    /**
     * 设备领用单ID
     */
    @ApiModelProperty(name="requisitionOrderId",value = "设备领用单ID")
    @Excel(name = "设备领用单ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "requisition_order_id")
    private Long requisitionOrderId;

    /**
     * 领用单编码(预留)
     */
    @ApiModelProperty(name="requisitionOrderCode",value = "领用单编码(预留)")
    @Excel(name = "领用单编码(预留)", height = 20, width = 30,orderNum="") 
    @Column(name = "requisition_order_code")
    private String requisitionOrderCode;

    /**
     * 领用人ID
     */
    @ApiModelProperty(name="requisitionUserId",value = "领用人ID")
    @Excel(name = "领用人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "requisition_user_id")
    private Long requisitionUserId;

    /**
     * 领用部门ID
     */
    @ApiModelProperty(name="deptId",value = "领用部门ID")
    @Excel(name = "领用部门ID", height = 20, width = 30,orderNum="") 
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 领用时间
     */
    @ApiModelProperty(name="requisitionTime",value = "领用时间")
    @Excel(name = "领用时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "requisition_time")
    private Date requisitionTime;

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
     * 设备领用明细
     */
    @ApiModelProperty(name="list",value = "设备领用明细")
    private List<EamRequisitionOrderDet> list = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}