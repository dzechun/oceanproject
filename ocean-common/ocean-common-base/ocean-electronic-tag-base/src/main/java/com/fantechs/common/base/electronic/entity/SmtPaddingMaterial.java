package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

/**
 * 上料单表
 * smt_padding_material
 * @author 53203
 * @date 2020-12-10 23:45:30
 */
@Data
@Table(name = "smt_padding_material")
public class SmtPaddingMaterial extends ValidGroup implements Serializable {
    /**
     * 上料单Id
     */
    @ApiModelProperty(name="paddingMaterialId",value = "上料单Id")
    @Id
    @Column(name = "padding_material_id")
    @NotNull(groups = update.class,message = "上料单id不能为空")
    private Long paddingMaterialId;

    /**
     * 上料单号
     */
    @ApiModelProperty(name="paddingMaterialCode",value = "上料单号")
    @Excel(name = "上料单号", height = 20, width = 30,orderNum="1")
    @Column(name = "padding_material_code")
    @NotBlank(message = "上料单号不能为空")
    private String paddingMaterialCode;

    /**
     * 工单号(预留)
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号(预留)")
    @Excel(name = "工单号(预留)", height = 20, width = 30,orderNum="2")
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="3")
    @Column(name = "material_code")
    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    /**
     * 分拣数量
     */
    @ApiModelProperty(name="quantity",value = "分拣数量")
    @Excel(name = "分拣数量", height = 20, width = 30,orderNum="4")
    @NotNull(message = "分拣数量不能为空")
    private BigDecimal quantity;

    /**
     * 状态(0-未开始，1-分拣中 2-已完成)
     */
    @ApiModelProperty(name="status",value = "状态(0-未开始，1-分拣中 2-已完成)")
    @Excel(name = "状态(0-未开始，1-分拣中 2-已完成)", height = 20, width = 30,orderNum="5")
    private Byte status;

    private static final long serialVersionUID = 1L;
}