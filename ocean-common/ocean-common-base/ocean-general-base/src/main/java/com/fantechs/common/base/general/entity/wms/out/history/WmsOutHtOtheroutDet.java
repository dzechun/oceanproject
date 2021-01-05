package com.fantechs.common.base.general.entity.wms.out.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 其他出库明细
 * wms_out_ht_otherout_det
 * @author hyc
 * @date 2020-12-31 09:54:52
 */
@Data
@Table(name = "wms_out_ht_otherout_det")
public class WmsOutHtOtheroutDet implements Serializable {
    @Id
    @Column(name = "ht_otherout_det_id")
    private Long htOtheroutDetId;

    /**
     * 其他出库单明细ID
     */
    @ApiModelProperty(name="otheroutDetId",value = "其他出库单明细ID")
    @Excel(name = "其他出库单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "otherout_det_id")
    private Long otheroutDetId;

    /**
     * 其他出库单ID
     */
    @ApiModelProperty(name="otheroutId",value = "其他出库单ID")
    @Excel(name = "其他出库单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "otherout_id")
    private Long otheroutId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 申请数量
     */
    @ApiModelProperty(name="planOutquantity",value = "申请数量")
    @Excel(name = "申请数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_outquantity")
    private BigDecimal planOutquantity;

    /**
     * 实发数量
     */
    @ApiModelProperty(name="realityOutquantity",value = "实发数量")
    @Excel(name = "实发数量", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_outquantity")
    private BigDecimal realityOutquantity;

    /**
     * 单价
     */
    @ApiModelProperty(name="unitPrice",value = "单价")
    @Excel(name = "单价", height = 20, width = 30,orderNum="") 
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    @ApiModelProperty(name="sum",value = "金额")
    @Excel(name = "金额", height = 20, width = 30,orderNum="") 
    private BigDecimal sum;

    /**
     * 仓库ID（出货仓库）
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID（出货仓库）")
    @Excel(name = "仓库ID（出货仓库）", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 仓库管理员ID
     */
    @ApiModelProperty(name="warehouseUserId",value = "仓库管理员ID")
    @Excel(name = "仓库管理员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_user_id")
    private Long warehouseUserId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

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
     * 其他出库单号
     */
    @ApiModelProperty(name="otheroutCode",value = "其他出库单号")
    @Excel(name = "其他出库单号", height = 20, width = 30,orderNum="1")
    @Transient
    private String otheroutCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Transient
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit" ,value="单位")
    @Excel(name = "单位", height = 20, width = 30,orderNum="5")
    private String unit;

    /**
     * 版本
     */
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30,orderNum="6")
    private String version;

    /**
     * 仓库名称（出货仓库）
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称（出货仓库）")
    @Excel(name = "仓库ID（出货仓库）", height = 20, width = 30,orderNum="11")
    @Column(name = "warehouse_name")
    private String warehouseName;

    /**
     * 仓库编码（出货仓库）
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码（出货仓库）")
    @Excel(name = "仓库编码（出货仓库）", height = 20, width = 30,orderNum="12")
    @Column(name = "warehouse_code")
    private String warehouseCode;

    /**
     * 仓库描述（出货仓库）
     */
    @ApiModelProperty(name="warehouseDesc",value = "仓库描述（出货仓库）")
    @Excel(name = "仓库描述（出货仓库）", height = 20, width = 30,orderNum="13")
    @Column(name = "warehouse_desc")
    private String warehouseDesc;

    /**
     * 仓库管理员名称
     */
    @ApiModelProperty(name="warehouseUserName",value = "仓库管理员名称")
    @Excel(name = "仓库管理员名称", height = 20, width = 30,orderNum="14")
    private String warehouseUserName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="15")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="16")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}