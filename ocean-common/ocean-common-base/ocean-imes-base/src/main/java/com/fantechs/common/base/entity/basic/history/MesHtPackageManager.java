package com.fantechs.common.base.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;

@Table(name = "mes_ht_package_manager")
@Data
public class MesHtPackageManager implements Serializable {
    /**
    * 包装管理履历id
    */
    @ApiModelProperty(value = "包装管理履历id",example = "包装管理履历id")
    @Column(name = "ht_package_manager_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    @Excel(name = "包装管理履历id")
    private Long htPackageManagerId;

    /**
    * 包装管理id
    */
    @ApiModelProperty(value = "包装管理id",example = "包装管理id")
    @Column(name = "package_manager_id")
    @Excel(name = "包装管理id")
    private Long packageManagerId;

    /**
    * 组织代码id
    */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "organization_id")
    @Excel(name = "组织代码id")
    private Long organizationId;

    /**
    * 包装编号
    */
    @ApiModelProperty(value = "包装编号",example = "包装编号")
    @Column(name = "package_manager_code")
    @Excel(name = "包装编号")
    private String packageManagerCode;

    /**
    * 包装条码
    */
    @ApiModelProperty(value = "包装条码",example = "包装条码")
    @Column(name = "bar_code")
    @Excel(name = "包装条码")
    private String barCode;

    /**
    * 工单id
    */
    @ApiModelProperty(value = "工单id",example = "工单id")
    @Column(name = "work_order_id")
    @Excel(name = "工单id")
    private Long workOrderId;

    /**
    * 物料id
    */
    @ApiModelProperty(value = "物料id",example = "物料id")
    @Column(name = "material_id")
    @Excel(name = "物料id")
    private Long materialId;

    /**
    * 数量
    */
    @ApiModelProperty(value = "数量",example = "数量")
    @Excel(name = "数量")
    private java.math.BigDecimal total;

    /**
    * 包装单位id
    */
    @ApiModelProperty(value = "包装单位id",example = "包装单位id")
    @Column(name = "packing_unit_id")
    @Excel(name = "包装单位id")
    private Long packingUnitId;

    /**
    * 包装规格ID
    */
    @ApiModelProperty(value = "包装规格ID",example = "包装规格ID")
    @Column(name = "package_specification_id")
    @Excel(name = "包装规格ID")
    private Long packageSpecificationId;

    /**
    * 父级id
    */
    @ApiModelProperty(value = "父级id",example = "父级id")
    @Column(name = "parent_id")
    @Excel(name = "父级id")
    private Long parentId;

    /**
    * 条码打印次数
    */
    @ApiModelProperty(value = "条码打印次数",example = "条码打印次数")
    @Column(name = "print_barcode_count")
    @Excel(name = "条码打印次数")
    private Integer printBarcodeCount;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述",example = "操作描述")
    @Excel(name = "操作描述")
    private String operation;

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