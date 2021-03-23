package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

/**
 * 盘点单信息表
 * wms_inner_stocktaking
 * @author 53203
 * @date 2021-03-22 16:37:12
 */
@Data
@Table(name = "wms_inner_stocktaking")
public class WmsInnerStocktaking extends ValidGroup implements Serializable {
    /**
     * 盘点单id
     */
    @ApiModelProperty(name="stocktakingId",value = "盘点单id")
    @Excel(name = "盘点单id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "stocktaking_id")
    private Long stocktakingId;

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="stocktakingCode",value = "盘点单号")
    @Excel(name = "盘点单号", height = 20, width = 30,orderNum="") 
    @Column(name = "stocktaking_code")
    private String stocktakingCode;

    /**
     * 仓库Id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库Id")
    @Excel(name = "仓库Id", height = 20, width = 30,orderNum="")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 盘点员Id
     */
    @ApiModelProperty(name="stockistId",value = "盘点员Id")
    @Excel(name = "盘点员Id", height = 20, width = 30,orderNum="") 
    @Column(name = "stockist_id")
    private Long stockistId;

    /**
     * 盘点方式(0、月盘 1、抽盘)
     */
    @ApiModelProperty(name="stocktakingMode",value = "盘点方式(0、月盘 1、抽盘)")
    @Excel(name = "盘点方式(0、月盘 1、抽盘)", height = 20, width = 30,orderNum="")
    @Column(name = "stocktaking_mode")
    private Byte stocktakingMode;

    /**
     * 盘点状态(0、待盘点 1、盘点中 2、盘点完成)
     */
    @ApiModelProperty(name="status",value = "盘点状态(0、待盘点 1、盘点中 2、盘点完成)")
    @Excel(name = "盘点状态(0、待盘点 1、盘点中 2、盘点完成)", height = 20, width = 30,orderNum="") 
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
    @Column(name = "organization_id")
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

    /**
     * 盘点单明细集合
     */
    @ApiModelProperty(name = "")
    private List<WmsInnerStocktakingDet> wmsInnerStocktakingDets = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}