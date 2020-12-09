package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

;

/**
 * 分拣单表
 * smt_sorting_list
 * @author 53203
 * @date 2020-12-08 17:44:15
 */
@Data
@Table(name = "smt_sorting")
public class SmtSorting extends ValidGroup implements Serializable {
    /**
     * 分拣单Id
     */
    @ApiModelProperty(name="sortingId",value = "分拣单Id")
    @Id
    @Column(name = "sorting_id")
    @NotNull(groups = update.class,message = "分拣单Id不能为空")
    private Long sortingId;

    /**
     * 分拣单号
     */
    @ApiModelProperty(name="sortingCode",value = "分拣单号")
    @Excel(name = "分拣单号", height = 20, width = 30,orderNum="1")
    @Column(name = "sorting_code")
    @NotBlank(message = "分拣单号不能为空")
    private String sortingCode;

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
    @Excel(name = "分拣数量", height = 20, width = 30,orderNum="5")
    @NotNull(message = "分拣数量不能为空")
    private BigDecimal quantity;

    /**
     * 状态(0-未开始，1-分拣中 2-已完成)
     */
    @ApiModelProperty(name="status",value = "状态(0-未开始，1-分拣中 2-已完成)")
    @Excel(name = "状态(0-未开始，1-分拣中 2-已完成)", height = 20, width = 30,orderNum="6")
    private Byte status;

    /**
     * 状态(0-未开始，1-分拣中 2-已完成)
     */
    @ApiModelProperty(name="status",value = "是否客户端传入(0-是，1-否)")
    @Transient
    private Byte updateStatus;

    private static final long serialVersionUID = 1L;
}