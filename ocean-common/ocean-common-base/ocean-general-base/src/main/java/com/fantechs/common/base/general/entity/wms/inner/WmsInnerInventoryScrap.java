package com.fantechs.common.base.general.entity.wms.inner;

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
import java.util.List;

;
;

/**
 * 盘存转报废单
 * wms_inner_inventory_scrap
 * @author hyc
 * @date 2021-03-10 16:14:47
 */
@Data
@Table(name = "wms_inner_inventory_scrap")
public class WmsInnerInventoryScrap extends ValidGroup implements Serializable {
    /**
     * 盘存转报废单ID
     */
    @ApiModelProperty(name="inventoryScrapId",value = "盘存转报废单ID")
    @NotNull(groups = update.class,message = "盘存转报废单ID不能为空")
    @Id
    @Column(name = "inventory_scrap_id")
    private Long inventoryScrapId;

    /**
     * 盘存转报废单号
     */
    @ApiModelProperty(name="inventoryScrapCode",value = "盘存转报废单号")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum="2")
    @Column(name = "inventory_scrap_code")
    private String inventoryScrapCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserId",value = "处理人")
    @Column(name = "processor_user_id")
    private Long processorUserId;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="inventoryScrapTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "transfer_slip_time")
    private Date inventoryScrapTime;

    /**
     * 单据状态（0-待报废 1-报废中 2-报废完成）
     */
    @ApiModelProperty(name="inventoryScrapStatus",value = "单据状态（0-待报废 1-报废中 2-报废完成）")
    @Excel(name = "单据状态（0-待报废 1-报废中 2-报废完成）", height = 20, width = 30,orderNum="5", replace = {"待报废_0","报废中_1","报废完成_2"})
    @Column(name = "inventory_scrap_status")
    private Byte inventoryScrapStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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

    private List<WmsInnerInventoryScrapDet> wmsInnerInventoryScrapDets;

    private static final long serialVersionUID = 1L;
}