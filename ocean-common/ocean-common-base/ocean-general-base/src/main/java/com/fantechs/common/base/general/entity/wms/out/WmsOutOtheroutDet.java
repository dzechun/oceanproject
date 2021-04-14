package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 其他出库明细
 * wms_out_otherout_det
 * @author 53203
 * @date 2020-12-25 16:40:35
 */
@Data
@Table(name = "wms_out_otherout_det")
public class WmsOutOtheroutDet extends ValidGroup implements Serializable {
    /**
     * 其他出库单明细ID
     */
    @ApiModelProperty(name="otheroutDetId",value = "其他出库单明细ID")
    @Id
    @Column(name = "otherout_det_id")
    @NotNull(groups = update.class,message = "出库单ID不能为空")
    private Long otheroutDetId;

    /**
     * 其他出库单ID
     */
    @ApiModelProperty(name="otheroutId",value = "其他出库单ID")
    @Column(name = "otherout_id")
    private Long otheroutId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 申请数量
     */
    @ApiModelProperty(name="planOutquantity",value = "申请数量")
    @Excel(name = "申请数量", height = 20, width = 30,orderNum="7")
    @Column(name = "plan_outquantity")
    private BigDecimal planOutquantity;

    /**
     * 实发数量
     */
    @ApiModelProperty(name="realityOutquantity",value = "实发数量")
    @Excel(name = "实发数量", height = 20, width = 30,orderNum="8")
    @Column(name = "reality_outquantity")
    private BigDecimal realityOutquantity;

    /**
     * 单价
     */
    @ApiModelProperty(name="unitPrice",value = "单价")
    @Excel(name = "单价", height = 20, width = 30,orderNum="9")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    @ApiModelProperty(name="sum",value = "金额")
    @Excel(name = "金额", height = 20, width = 30,orderNum="10")
    private BigDecimal sum;

    /**
     * 仓库ID（出货仓库）
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID（出货仓库）")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 仓库管理员ID
     */
    @ApiModelProperty(name="warehouseUserId",value = "仓库管理员ID")
    @Column(name = "warehouse_user_id")
    private Long warehouseUserId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

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
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="17")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="18",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="19",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    @ApiModelProperty(name="outPalletList",value = "已出库栈板集合")
    private List<String> outPalletList;

    private static final long serialVersionUID = 1L;
}