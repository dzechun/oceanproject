package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "base_ht_storage")
@Data
public class BaseHtStorage implements Serializable {
    private static final long serialVersionUID = 8010325835466668444L;
    /**
     * 储位历史ID
     */
    @Id
    @Column(name = "ht_storage_id")
    @ApiModelProperty(name = "htStorageId",value = "储位历史ID")
    private Long htStorageId;

    /**
     * 储位ID
     */
    @Column(name = "storage_id")
    @ApiModelProperty(name = "storageId",value = "储位ID")
    private Long storageId;

    /**
     * 储位编码
     */
    @Column(name = "storage_code")
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageCode;

    /**
     * 储位名称
     */
    @Column(name = "storage_name")
    @ApiModelProperty(name = "storageName",value = "储位名称")
    private String storageName;

    /**
     * 层级
     */
    @Column(name = "level")
    @ApiModelProperty(name = "level",value = "层级")
    private String level;

    /**
     * 储位描述
     */
    @Column(name = "storage_desc")
    @ApiModelProperty(name = "storageDesc",value = "储位描述")
    private String storageDesc;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id")
    @ApiModelProperty(name = "warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库编码
     */
    @Transient
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库类型
     */
    @Transient
    @ApiModelProperty(name = "warehouseCategory",value = "仓库类型")
    private Long warehouseCategory;

    /**
     * 仓库区域ID
     */
    @Column(name = "warehouse_area_id")
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域ID")
    private Long warehouseAreaId;

    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    /**
     * 仓库区域编码
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaCode" ,value="仓库区域编码")
    private String warehouseAreaCode;

    /**
     * 容量
     */
    @ApiModelProperty(name="capacity",value = "容量")
    @Excel(name = "容量", height = 20, width = 30)
    private BigDecimal capacity;

    /**
     * 温度
     */
    @ApiModelProperty(name="temperature",value = "温度")
    @Excel(name = "温度", height = 20, width = 30)
    private BigDecimal temperature;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit",value = "单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;

    /**
     * 库位类型（1-存货 2-收货 3-发货）
     */
    @ApiModelProperty(name="storageType",value = "库位类型（1-存货 2-收货 3-发货）")
    @Column(name = "storage_type")
    private Byte storageType;

    /**
     * 工作区
     */
    @ApiModelProperty(name="workingAreaId",value = "工作区")
    @Column(name = "working_area_id")
    private Long workingAreaId;

    /**
     * 工作区编码
     */
    @Transient
    @ApiModelProperty(name="workingAreaCode" ,value="工作区编码")
    private String workingAreaCode;

    /**
     * 巷道
     */
    @ApiModelProperty(name="roadway",value = "巷道")
    @Column(name = "roadway")
    private Integer roadway;

    /**
     * 排
     */
    @ApiModelProperty(name="rowNo",value = "排")
    @Column(name = "row_no")
    private Integer rowNo;

    /**
     * 列
     */
    @ApiModelProperty(name="columnNo",value = "列")
    @Column(name = "column_no")
    private Integer columnNo;

    /**
     * 层
     */
    @ApiModelProperty(name="levelNo",value = "层")
    @Column(name = "level_no")
    private Integer levelNo;

    /**
     * 上架动线号
     */
    @ApiModelProperty(name="putawayMoveLineNo",value = "上架动线号")
    @Column(name = "putaway_move_line_no")
    private Integer putawayMoveLineNo;

    /**
     * 拣货动线号
     */
    @ApiModelProperty(name="pickingMoveLineNo",value = "拣货动线号")
    @Column(name = "picking_move_line_no")
    private Integer pickingMoveLineNo;

    /**
     * 盘点动线号
     */
    @ApiModelProperty(name="stockMoveLineNo",value = "盘点动线号")
    @Column(name = "stock_move_line_no")
    private Integer stockMoveLineNo;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name="ifStockLock",value = "盘点锁(0-否 1-是)")
    @Column(name = "if_stock_lock")
    private Byte ifStockLock;

    /**
     * 剩余载重
     */
    @ApiModelProperty(name="surplusLoad",value = "剩余载重")
    @Column(name = "surplus_load")
    private BigDecimal surplusLoad;

    /**
     * 剩余容积
     */
    @ApiModelProperty(name="surplusVolume",value = "剩余容积")
    @Column(name = "surplus_volume")
    private BigDecimal surplusVolume;

    /**
     * 剩余可放托盘数
     */
    @ApiModelProperty(name="surplusCanPutSalver",value = "剩余可放托盘数")
    @Column(name = "surplus_can_put_salver")
    private Integer surplusCanPutSalver;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name = "modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name = "isDelete",value = "逻辑删除")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;
}
