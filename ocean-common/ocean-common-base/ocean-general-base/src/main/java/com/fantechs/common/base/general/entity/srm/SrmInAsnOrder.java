package com.fantechs.common.base.general.entity.srm;

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
 * Advanced Shipping Note预先发货清单
 * wms_in_asn_order
 * @author 81947
 * @date 2021-11-25 09:19:10
 */
@Data
@Table(name = "wms_in_asn_order")
public class SrmInAsnOrder extends ValidGroup implements Serializable {
    /**
     * ASN单ID
     */
    @ApiModelProperty(name="asnOrderId",value = "ASN单ID")
    @Excel(name = "ASN单ID", height = 20, width = 30) 
    @Id
    @Column(name = "asn_order_id")
    private Long asnOrderId;

    /**
     * 来源单据ID
     */
    @ApiModelProperty(name="sourceOrderId",value = "来源单据ID")
    @Excel(name = "来源单据ID", height = 20, width = 30) 
    @Column(name = "source_order_id")
    private Long sourceOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="corSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    @Column(name = "core_source_sys_order_type_code")
    private String corSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;


    /**
     * 货主信息
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主信息")
    @Excel(name = "货主信息", height = 20, width = 30) 
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30) 
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30) 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 单据类型ID
     */
    @ApiModelProperty(name="orderTypeId",value = "单据类型ID")
    @Excel(name = "单据类型ID", height = 20, width = 30) 
    @Column(name = "order_type_id")
    private Long orderTypeId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Excel(name = "库位ID", height = 20, width = 30) 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * ASN单号
     */
    @ApiModelProperty(name="asnCode",value = "ASN单号")
    @Excel(name = "ASN单号", height = 20, width = 30,orderNum="1")
    @Column(name = "asn_code")
    private String asnCode;

    /**
     * 预计到货(arrival of good)日期
     */
    @ApiModelProperty(name="planAgoDate",value = "预计到货(arrival of good)日期")
    @Excel(name = "预计到货(arrival of good)日期", height = 20, width = 30)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_ago_date")
    private Date planAgoDate;

    /**
     * 开始收货日期
     */
    @ApiModelProperty(name="startReceivingDate",value = "开始收货日期")
    @Excel(name = "开始收货日期", height = 20, width = 30)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_receiving_date")
    private Date startReceivingDate;

    /**
     * 结束收货日期
     */
    @ApiModelProperty(name="endReceivingDate",value = "结束收货日期")
    @Excel(name = "结束收货日期", height = 20, width = 30)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_receiving_date")
    private Date endReceivingDate;

    /**
     * 发货日期
     */
    @ApiModelProperty(name="deliverDate",value = "发货日期")
    @Excel(name = "发货日期", height = 20, width = 30,orderNum="3")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "deliver_date")
    private Date deliverDate;

    /**
     * 发货人名称
     */
    @ApiModelProperty(name="shipperName",value = "发货人名称")
    @Excel(name = "发货人名称", height = 20, width = 30,orderNum="2")
    @Column(name = "shipper_name")
    private String shipperName;

    /**
     * 发货人用户ID
     */
    @ApiModelProperty(name="shipperUserId",value = "发货人用户ID")
    @Excel(name = "发货人用户ID", height = 20, width = 30) 
    @Column(name = "shipper_user_id")
    private Long shipperUserId;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    @Excel(name = "联系人名称", height = 20, width = 30) 
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系人电话")
    @Excel(name = "联系人电话", height = 20, width = 30) 
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 单据状态(1-保存 2-提交 3-审核通过 4-审核未通过 5-已预约 6-发货)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-保存 2-提交 3-审核通过 4-审核未通过 5-已预约 6-发货)")
    @Excel(name = "单据状态(1-保存 2-提交 3-审核通过 4-审核未通过 5-已预约 6-发货)", height = 20, width = 30,orderNum="4")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 文件ID
     */
    @ApiModelProperty(name="fileId",value = "文件ID")
    @Excel(name = "文件ID", height = 20, width = 30) 
    @Column(name = "file_id")
    private Long fileId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30) 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30) 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30) 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30) 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30) 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30) 
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}