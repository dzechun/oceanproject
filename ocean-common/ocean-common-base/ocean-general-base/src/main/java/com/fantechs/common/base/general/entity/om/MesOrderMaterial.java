package com.fantechs.common.base.general.entity.om;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;

@Table(name = "mes_order_material")
@Data
public class MesOrderMaterial implements Serializable {
    /**
    * 销售订单与物料id
    */
    @ApiModelProperty(value = "销售订单与物料id",example = "销售订单与物料id")
    @Column(name = "order_material_id")
    @Id
    @Excel(name = "销售订单与物料id")
    private Long orderMaterialId;

    /**
    * 组织代码id
    */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "org_id")
    @Excel(name = "组织代码id")
    private Long organizationId;

    /**
    * 订单id
    */
    @ApiModelProperty(value = "订单id",example = "订单id")
    @Column(name = "order_id")
    @Excel(name = "订单id")
    private Long orderId;

    /**
    * 物料id
    */
    @ApiModelProperty(value = "物料id",example = "物料id")
    @Column(name = "material_id")
    @Excel(name = "物料id")
    private Long materialId;

    /**
    * 产品数量
    */
    @ApiModelProperty(value = "产品数量",example = "产品数量")
    @Excel(name = "产品数量")
    private java.math.BigDecimal total;

    /**
    * 编箱号
    */
    @ApiModelProperty(value = "编箱号",example = "编箱号")
    @Column(name = "box_code")
    @Excel(name = "编箱号")
    private String boxCode;

    /**
     * 包装规格id
     */
    @ApiModelProperty(value = "包装规格id",example = "包装规格id")
    @Column(name = "package_specification_id")
    @Excel(name = "包装规格id")
    private Long packageSpecificationId;

    /**
    * 尺码(CM)
    */
    @ApiModelProperty(value = "尺码(CM)",example = "尺码(CM)")
    @Excel(name = "尺码(CM)")
    private java.math.BigDecimal size;

    /**
    * 毛重(KG)
    */
    @ApiModelProperty(value = "毛重(KG)",example = "毛重(KG)")
    @Column(name = "rough_weight")
    @Excel(name = "毛重(KG)")
    private java.math.BigDecimal roughWeight;

    /**
    * 净重(KG)
    */
    @ApiModelProperty(value = "净重(KG)",example = "净重(KG)")
    @Column(name = "net_weight")
    @Excel(name = "净重(KG)")
    private java.math.BigDecimal netWeight;

    /**
    * 产品规格文件
    */
    @ApiModelProperty(value = "产品规格文件",example = "产品规格文件")
    @Column(name = "spec_file")
    @Excel(name = "产品规格文件")
    private String specFile;

    /**
     * 排产状态（0、待排产 1、已排产）
     */
    @ApiModelProperty(value = "排产状态（0、待排产 1、已排产）",example = "排产状态（0、待排产 1、已排产）")
    @Column(name = "schedule_status")
    @Excel(name = "排产状态（0、待排产 1、已排产）")
    private Byte scheduleStatus;

    /**
    * 逻辑删除（0、删除 1、正常）
    */
    @ApiModelProperty(value = "逻辑删除（0、删除 1、正常）",example = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    @Excel(name = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",example = "备注")
    @Excel(name = "备注")
    private String remark;

    /**
    * 创建人ID
    */
    @ApiModelProperty(value = "创建人ID",example = "创建人ID")
    @Column(name = "create_user_id")
    @Excel(name = "创建人ID")
    private Long createUserId;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",example = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    private java.util.Date createTime;

    /**
    * 修改人ID
    */
    @ApiModelProperty(value = "修改人ID",example = "修改人ID")
    @Column(name = "modified_user_id")
    @Excel(name = "修改人ID")
    private Long modifiedUserId;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间",example = "修改时间")
    @Column(name = "modified_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间")
    private java.util.Date modifiedTime;

}