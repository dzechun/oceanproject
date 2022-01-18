package com.fantechs.common.base.general.entity.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 送货预约
 * srm_delivery_appoint
 * @author 81947
 * @date 2021-11-24 11:20:08
 */
@Data
@Table(name = "srm_delivery_appoint")
public class SrmDeliveryAppoint extends ValidGroup implements Serializable {
    /**
     * 送货预约ID
     */
    @ApiModelProperty(name="deliveryAppointId",value = "送货预约ID")
    @Id
    @Column(name = "delivery_appoint_id")
    private Long deliveryAppointId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 车牌号码
     */
    @ApiModelProperty(name="carLicenseCode",value = "车牌号码")
    @Excel(name = "车牌号码", height = 20, width = 30,orderNum="2")
    @Column(name = "car_license_code")
    private String carLicenseCode;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    @Excel(name = "联系人名称", height = 20, width = 30,orderNum="3")
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系电话")
    @Excel(name = "联系电话", height = 20, width = 30,orderNum="4")
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 送货仓库ID
     */
    @ApiModelProperty(name="deliveryWarehouseId",value = "送货仓库ID")
    @Column(name = "delivery_warehouse_id")
    private Long deliveryWarehouseId;

    /**
     * 送货地址
     */
    @ApiModelProperty(name="deliveryAddress",value = "送货地址")
    @Excel(name = "送货地址", height = 20, width = 30,orderNum="6")
    @Column(name = "delivery_address")
    private String deliveryAddress;

    /**
     * 预约日期
     */
    @ApiModelProperty(name="appointDate",value = "预约日期")
    @Excel(name = "预约日期", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd")
    @Column(name = "appoint_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date appointDate;

    /**
     * 预约时间段-开始
     */
    @ApiModelProperty(name="appointStartTime",value = "预约时间段-开始")
    @Excel(name = "预约时间段-开始", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "appoint_start_time")
    private Date appointStartTime;

    /**
     * 预约时间段-结束
     */
    @ApiModelProperty(name="appointEndTime",value = "预约时间段-结束")
    @Excel(name = "预约时间段-结束", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "appoint_end_time")
    private Date appointEndTime;

    /**
     * 预约状态(1-预约待审核 2-预约失败 3-已预约 4-取消待审核5-已取消 6-发货完成)
     */
    @ApiModelProperty(name="appointStatus",value = "预约状态(1-预约待审核 2-预约失败 3-已预约 4-取消待审核5-已取消 6-发货完成)")
    @Excel(name = "预约状态(1-预约待审核 2-预约失败 3-已预约 4-取消待审核5-已取消 6-发货完成)", height = 20, width = 30,orderNum="10")
    @Column(name = "appoint_status")
    private Byte appointStatus;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="14",exportFormat ="yyyy-MM-dd HH:mm:ss")
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