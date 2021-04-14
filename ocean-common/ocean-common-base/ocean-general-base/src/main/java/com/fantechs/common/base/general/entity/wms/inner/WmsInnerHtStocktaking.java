package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 盘点单信息履历表
 * wms_inner_ht_stocktaking
 * @author 53203
 * @date 2021-03-22 17:05:09
 */
@Data
@Table(name = "wms_inner_ht_stocktaking")
public class WmsInnerHtStocktaking extends ValidGroup implements Serializable {
    /**
     * 盘点单履历id
     */
    @ApiModelProperty(name="htStocktakingId",value = "盘点单履历id")
    @Excel(name = "盘点单履历id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_stocktaking_id")
    private Long htStocktakingId;

    /**
     * 盘点单id
     */
    @ApiModelProperty(name="stocktakingId",value = "盘点单id")
    @Excel(name = "盘点单id", height = 20, width = 30,orderNum="") 
    @Column(name = "stocktaking_id")
    private Long stocktakingId;

    /**
     * 仓库Id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库Id")
    @Excel(name = "仓库Id", height = 20, width = 30,orderNum="")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 盘点方式(0、月盘 1、抽盘)
     */
    @ApiModelProperty(name="stocktakingMode",value = "盘点方式(0、月盘 1、抽盘)")
    @Excel(name = "盘点方式(0、月盘 1、抽盘)", height = 20, width = 30,orderNum="")
    @Column(name = "stocktaking_mode")
    private Byte stocktakingMode;

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="stocktakingCode",value = "盘点单号")
    @Excel(name = "盘点单号", height = 20, width = 30,orderNum="") 
    @Column(name = "stocktaking_code")
    private String stocktakingCode;

    /**
     * 盘点员Id
     */
    @ApiModelProperty(name="stockistId",value = "盘点员Id")
    @Excel(name = "盘点员Id", height = 20, width = 30,orderNum="") 
    @Column(name = "stockist_id")
    private Long stockistId;

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

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="")
    @Transient
    private String warehouseName;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="")
    @Transient
    private String warehouseCode;

    /**
     * 仓库描述
     */
    @ApiModelProperty(name="warehouseDesc",value = "仓库描述")
    @Excel(name = "仓库描述", height = 20, width = 30,orderNum="")
    @Transient
    private String warehouseDesc;

    /**
     * 盘点员名称
     */
    @ApiModelProperty(name="stockistName",value = "盘点员名称")
    @Excel(name = "盘点员名称", height = 20, width = 30,orderNum="")
    @Transient
    private String stockistName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 组织代码
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织代码")
    private String organizationCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}