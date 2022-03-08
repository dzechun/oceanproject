package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "base_storage")
@Data
public class BaseStorage extends ValidGroup implements Serializable {
    private static final long serialVersionUID = -3338572709820716313L;
    /**
     * 库位ID
     */
    @Id
    @Column(name = "storage_id")
    @ApiModelProperty(name = "storageId",value = "库位ID")
    @NotNull(groups = update.class,message = "库位id不能为空")
    private Long storageId;

    /**
     * 库位编码
     */
    @Column(name = "storage_code")
    @ApiModelProperty(name = "storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="1")
    @NotBlank(message = "库位编码不能为空")
    private String storageCode;

    /**
     * 库位名称
     */
    @Column(name = "storage_name")
    @ApiModelProperty(name = "storageName",value = "库位名称")
    private String storageName;

    /**
     * 层级
     */
    @Column(name = "level")
    @ApiModelProperty(name = "level",value = "层级")
    private String level;

    /**
     * 库位描述
     */
    @Column(name = "storage_desc")
    @ApiModelProperty(name = "storageDesc",value = "库位描述")
    @Excel(name = "库位描述", height = 20, width = 30,orderNum="17")
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
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="5")
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
    @ApiModelProperty(name="warehouseCategory",value = "仓库类型")
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
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="3")
    private String warehouseAreaName;

    /**
     * 仓库区域名编码
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaCode" ,value="仓库区域名编码")
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
    @Excel(name = " 库位类型（1-存货 2-收货 3-发货）", height = 20, width = 30,orderNum="2",replace = {"存货_1","收货_2", "发货_3"})
    private Byte storageType;

    /**
     * 产品存储类型(1-A类 2-B类 3-C类 4-D类)
     */
    @ApiModelProperty(name="materialStoreType",value = "产品存储类型(1-A类 2-B类 3-C类 4-D类)")
    @Column(name = "material_store_type")
    private Byte materialStoreType;

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
    @Excel(name = "工作区编码", height = 20, width = 30,orderNum="4")
    private String workingAreaCode;

    /**
     * 巷道
     */
    @ApiModelProperty(name="roadway",value = "巷道")
    @Column(name = "roadway")
    @Excel(name = "巷道", height = 20, width = 30,orderNum="6")
    private Integer roadway;

    /**
     * 排
     */
    @ApiModelProperty(name="rowNo",value = "排")
    @Column(name = "row_no")
    @Excel(name = "排", height = 20, width = 30,orderNum="7")
    private Integer rowNo;

    /**
     * 列
     */
    @ApiModelProperty(name="columnNo",value = "列")
    @Column(name = "column_no")
    @Excel(name = "列", height = 20, width = 30,orderNum="8")
    private Integer columnNo;

    /**
     * 层
     */
    @ApiModelProperty(name="levelNo",value = "层")
    @Column(name = "level_no")
    @Excel(name = "层", height = 20, width = 30,orderNum="9")
    private Integer levelNo;

    /**
     * 上架动线号
     */
    @ApiModelProperty(name="putawayMoveLineNo",value = "上架动线号")
    @Column(name = "putaway_move_line_no")
    @Excel(name = "上架动线号", height = 20, width = 30,orderNum="10")
    private Integer putawayMoveLineNo;

    /**
     * 拣货动线号
     */
    @ApiModelProperty(name="pickingMoveLineNo",value = "拣货动线号")
    @Column(name = "picking_move_line_no")
    @Excel(name = "拣货动线号", height = 20, width = 30,orderNum="11")
    private Integer pickingMoveLineNo;

    /**
     * 盘点动线号
     */
    @ApiModelProperty(name="stockMoveLineNo",value = "盘点动线号")
    @Column(name = "stock_move_line_no")
    @Excel(name = "盘点动线号", height = 20, width = 30,orderNum="12")
    private Integer stockMoveLineNo;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name="ifStockLock",value = "盘点锁(0-否 1-是)")
    @Column(name = "if_stock_lock")
    @Excel(name = "盘点锁(0-否 1-是)", height = 20, width = 30,orderNum="13",replace = {"否_0", "是_1"})
    private Byte ifStockLock;

    /**
     * 剩余载重
     */
    @ApiModelProperty(name="surplusLoad",value = "剩余载重")
    @Column(name = "surplus_load")
    @Excel(name = "剩余载重", height = 20, width = 30,orderNum="14")
    private BigDecimal surplusLoad;

    /**
     * 剩余容积
     */
    @ApiModelProperty(name="surplusVolume",value = "剩余容积")
    @Column(name = "surplus_volume")
    @Excel(name = "剩余容积", height = 20, width = 30,orderNum="15")
    private BigDecimal surplusVolume;

    /**
     * 剩余可放托盘数
     */
    @ApiModelProperty(name="surplusCanPutSalver",value = "剩余可放托盘数")
    @Column(name = "surplus_can_put_salver")
    @Excel(name = "剩余可放托盘数", height = 20, width = 30,orderNum="16")
    private Integer surplusCanPutSalver;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
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
    @Excel(name = "备注", height = 20, width = 30,orderNum="19")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,orderNum="18",replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="20")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="21",exportFormat = "yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="22")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="23",exportFormat = "yyyy-MM-dd HH:mm:ss")
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

    @ApiModelProperty(name = "logicId",value = "erp逻辑id")
    @Column(name = "logic_id")
    private Long logicId;

    @Transient
    @ApiModelProperty(name = "logicName",value = "erp逻辑仓名称")
    private String logicName;

    @ApiModelProperty(name = "proLineId",value = "产线id")
    @Column(name = "pro_line_id")
    private Long proLineId;

    @ApiModelProperty(name = "isHeelpiece",value = "是否底垫（1-否 2-是）")
    @Column(name = "is_heelpiece")
    private Byte isHeelpiece;

    @Transient
    @ApiModelProperty(name = "proName",value = "产线名称")
    private String proName;

}
