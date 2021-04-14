package com.fantechs.common.base.general.entity.qms;

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
import java.math.BigDecimal;
import java.util.Date;


/**
 * 检验项目明细表
 * @date 2020-12-25 15:39:28
 */
@Data
@Table(name = "qms_inspection_item_det")
public class QmsInspectionItemDet extends ValidGroup implements Serializable {
    /**
     * 检验项目明细ID
     */
    @ApiModelProperty(name="inspectionItemDetId",value = "检验项目明细ID")
    @Id
    @Column(name = "inspection_item_det_id")
    private Long inspectionItemDetId;

    /**
     * 检验项目ID
     */
    @ApiModelProperty(name="inspectionItemId",value = "检验项目ID")
    @Column(name = "inspection_item_id")
    @NotNull(message = "检验项目ID不能为空")
    private Long inspectionItemId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    @NotNull(message = "物料ID不能为空")
    private Long materialId;

    /**
     * 最小包装数量
     */
    @ApiModelProperty(name="minPackageNumber",value = "最小包装数量")
    @Excel(name = "最小包装数量", height = 20, width = 30,orderNum="5")
    @Column(name = "min_package_number")
    private BigDecimal minPackageNumber;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit",value = "单位")
    @Excel(name = "单位", height = 20, width = 30,orderNum="6")
    private String unit;

    /**
     * 安全库存
     */
    @ApiModelProperty(name="safeStock",value = "安全库存")
    @Excel(name = "安全库存", height = 20, width = 30,orderNum="7")
    @Column(name = "safe_stock")
    private BigDecimal safeStock;

    /**
     * 特殊分类
     */
    @ApiModelProperty(name="specialSort",value = "特殊分类")
    @Excel(name = "特殊分类", height = 20, width = 30,orderNum="8")
    @Column(name = "special_sort")
    private Long specialSort;

    /**
     * 保质期
     */
    @ApiModelProperty(name="expirationDate",value = "保质期")
    @Excel(name = "保质期", height = 20, width = 30,orderNum="9")
    @Column(name = "expiration_date")
    private BigDecimal expirationDate;

    /**
     * 最大回温次数
     */
    @ApiModelProperty(name="rewarmingTime",value = "最大回温次数")
    @Excel(name = "最大回温次数", height = 20, width = 30,orderNum="10")
    @Column(name = "rewarming_time")
    private String rewarmingTime;

    /**
     * 元件尺寸
     */
    @ApiModelProperty(name="elementSize",value = "元件尺寸")
    @Excel(name = "元件尺寸", height = 20, width = 30,orderNum="11")
    @Column(name = "element_size")
    private String elementSize;

    /**
     * 湿敏等级
     */
    @ApiModelProperty(name="humidityClass",value = "湿敏等级")
    @Excel(name = "湿敏等级", height = 20, width = 30,orderNum="12")
    @Column(name = "humidity_class")
    private Long humidityClass;

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
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="14",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="16",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
