package com.fantechs.common.base.entity.storage;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

@Table(name = "wms_storage_bills")
@Data
public class WmsStorageBills extends ValidGroup implements Serializable {
    /**
    * 仓库清单id
    */
    @ApiModelProperty(value = "仓库清单id",example = "仓库清单id")
    @Column(name = "storage_bills_id")
    @Id
    @Excel(name = "仓库清单id")
    private Long storageBillsId;

    /**
    * 单号
    */
    @ApiModelProperty(value = "单号",example = "单号")
    @Column(name = "storage_bills_code")
    @Excel(name = "单号")
    private String storageBillsCode;

    /**
    * 入库数量总计
    */
    @ApiModelProperty(value = "入库数量总计",example = "入库数量总计")
    @Excel(name = "入库数量总计")
    @NotNull(message = "入库数量总计不能为空")
    private java.math.BigDecimal total;

    /**
    * 待入库物料种类总计
    */
    @ApiModelProperty(value = "待入库物料种类总计",example = "待入库物料种类总计")
    @Column(name = "material_type_total")
    @Excel(name = "待入库物料种类总计")
    @NotNull(message = "待入库物料种类总计不能为空")
    private java.math.BigDecimal materialTypeTotal;

    /**
    * 已入库物料种类总计
    */
    @ApiModelProperty(value = "已入库物料种类总计",example = "已入库物料种类总计")
    @Column(name = "finished_total")
    @Excel(name = "已入库物料种类总计")
    private java.math.BigDecimal finishedTotal;

    /**
    * 单据类型（1、入库计划 2、收获计划 3、完工入库计划 4、销售退货计划）
    */
    @ApiModelProperty(value = "单据类型（1、入库计划 2、收获计划 3、完工入库计划 4、销售退货计划）",example = "单据类型（1、入库计划 2、收获计划 3、完工入库计划 4、销售退货计划）")
    @Excel(name = "单据类型（1、入库计划 2、收获计划 3、完工入库计划 4、销售退货计划）")
    @NotNull(message = "单据类型不能为空")
    private Integer type;

    /**
    * 供应商或客户Id
    */
    @ApiModelProperty(value = "供应商或客户Id",example = "供应商或客户Id")
    @Column(name = "supplier_id")
    @Excel(name = "供应商或客户Id")
    private Long supplierId;

    /**
    * 入库状态（1、待完成 2、进行中 3、完成）
    */
    @ApiModelProperty(value = "入库状态（1、待完成 2、进行中 3、完成）",example = "入库状态（1、待完成 2、进行中 3、完成）")
    @Excel(name = "入库状态（1、待完成 2、进行中 3、完成）")
    private Byte status;

    /**
    * 逻辑删除（0、删除 1、正常）
    */
    @ApiModelProperty(value = "逻辑删除（0、删除 1、正常）",example = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    @Excel(name = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间")
    private java.util.Date modifiedTime;

}