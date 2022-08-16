package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 设备报废单
 * eam_equipment_scrap_order
 * @author admin
 * @date 2021-08-21 13:52:41
 */
@Data
@Table(name = "eam_equipment_scrap_order")
public class EamEquipmentScrapOrder extends ValidGroup implements Serializable {
    /**
     * 设备报废单ID
     */
    @ApiModelProperty(name="equipmentScrapOrderId",value = "设备报废单ID")
    @Id
    @Column(name = "equipment_scrap_order_id")
    @NotNull(groups = update.class,message = "设备报废单ID不能为空")
    private Long equipmentScrapOrderId;

    /**
     * 设备报废单编码
     */
    @ApiModelProperty(name="equipmentScrapOrderCode",value = "设备报废单编码")
    @Excel(name = "设备报废单编码", height = 20, width = 30,orderNum="1")
    @Column(name = "equipment_scrap_order_code")
    private String equipmentScrapOrderCode;

    /**
     * 申请用户ID
     */
    @ApiModelProperty(name="applyUserId",value = "申请用户ID")
    @Column(name = "apply_user_id")
    private Long applyUserId;

    /**
     * 申请部门ID
     */
    @ApiModelProperty(name="applyDeptId",value = "申请部门ID")
    @Column(name = "apply_dept_id")
    private Long applyDeptId;

    /**
     * 申请时间
     */
    @ApiModelProperty(name="applyTime",value = "申请时间")
    @Excel(name = "申请时间", height = 20, width = 30,orderNum="4",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "apply_time")
    private Date applyTime;

    /**
     * 使用人ID
     */
    @ApiModelProperty(name="useUserId",value = "使用人ID")
    @Column(name = "use_user_id")
    private Long useUserId;

    /**
     * 单据状态(1-待确认 2-已确认)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待确认 2-已确认)")
    @Excel(name = "单据状态(1-待确认 2-已确认)", height = 20, width = 30,orderNum="7",replace = {"待确认_1","已确认_2"})
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 使用部门ID
     */
    @ApiModelProperty(name="useDeptId",value = "使用部门ID")
    @Column(name = "use_dept_id")
    private Long useDeptId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}