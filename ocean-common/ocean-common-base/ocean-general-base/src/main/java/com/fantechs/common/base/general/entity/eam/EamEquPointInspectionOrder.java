package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDetDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 设备点检单
 * eam_equ_point_inspection_order
 * @author bgkun
 * @date 2021-08-20 14:19:03
 */
@Data
@Table(name = "eam_equ_point_inspection_order")
public class EamEquPointInspectionOrder extends ValidGroup implements Serializable {
    /**
     * 设备点检单ID
     */
    @ApiModelProperty(name="equPointInspectionOrderId",value = "设备点检单ID")
    @Id
    @Column(name = "equ_point_inspection_order_id")
    private Long equPointInspectionOrderId;

    /**
     * 设备点检单号
     */
    @ApiModelProperty(name="equPointInspectionOrderCode",value = "设备点检单号")
    @Excel(name = "设备点检单号", height = 20, width = 30,orderNum="1")
    @Column(name = "equ_point_inspection_order_code")
    private String equPointInspectionOrderCode;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 设备条码ID
     */
    @ApiModelProperty(name="equipmentBarcodeId",value = "设备条码ID")
    @Column(name = "equipment_barcode_id")
    private Long equipmentBarcodeId;

    /**
     * 设备点检项目ID
     */
    @ApiModelProperty(name="equPointInspectionProjectId",value = "设备点检项目ID")
    @Column(name = "equ_point_inspection_project_id")
    private Long equPointInspectionProjectId;

    /**
     * 单据状态(1-待点检 2-已点检)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待点检 2-已点检)")
    @Excel(name = "单据状态(1-待点检 2-已点检)", height = 20, width = 30,orderNum="8")
    @Column(name = "order_status")
    private Byte orderStatus;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;

    @Transient
    @ApiModelProperty(name="orderDets",value = "点检单明细")
    private List<EamEquPointInspectionOrderDetDto> orderDets;
}