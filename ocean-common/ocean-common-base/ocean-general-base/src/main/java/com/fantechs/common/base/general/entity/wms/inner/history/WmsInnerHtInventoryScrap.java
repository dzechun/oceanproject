package com.fantechs.common.base.general.entity.wms.inner.history;

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

/**
 * 盘存转报废单
 * wms_inner_ht_inventory_scrap
 * @author hyc
 * @date 2021-03-10 16:14:46
 */
@Data
@Table(name = "wms_inner_ht_inventory_scrap")
public class WmsInnerHtInventoryScrap extends ValidGroup implements Serializable {
    /**
     * 盘存转报废单履历ID
     */
    @ApiModelProperty(name="htInventoryScrapId",value = "盘存转报废单履历ID")
    @Id
    @NotNull(groups = update.class,message = "盘存转报废单履历ID不能为空")
    @Column(name = "ht_inventory_scrap_id")
    private Long htInventoryScrapId;

    /**
     * 盘存转报废单ID
     */
    @ApiModelProperty(name="inventoryScrapId",value = "盘存转报废单ID")
    @Excel(name = "盘存转报废单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inventory_scrap_id")
    private Long inventoryScrapId;

    /**
     * 盘存转报废单号
     */
    @ApiModelProperty(name="inventoryScrapCode",value = "盘存转报废单号")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum="") 
    @Column(name = "inventory_scrap_code")
    private String inventoryScrapCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserId",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="") 
    @Column(name = "processor_user_id")
    private Long processorUserId;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="transferSlipTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_slip_time")
    private Date transferSlipTime;

    /**
     * 单据状态（0-待报废 1-报废中 2-报废完成）
     */
    @ApiModelProperty(name="transferSlipStatus",value = "单据状态（0-待报废 1-报废中 2-报废完成）")
    @Excel(name = "单据状态（0-待报废 1-报废中 2-报废完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_slip_status")
    private Byte transferSlipStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
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
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long organizationId;

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

    private static final long serialVersionUID = 1L;

    /**
     * 处理人名称
     */
    @ApiModelProperty(name="processorUserName",value = "处理人名称")
    private String processorUserName;

    /**
     * 组织代码
     */
    @ApiModelProperty(name="organizationId",value = "组织代码")
    private String organizationCode;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    private String modifiedUserName;
}