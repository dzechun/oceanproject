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

/**
 * 分拣单表
 * smt_sorting_list
 * @author 53203
 * @date 2020-12-08 17:44:15
 */
@Data
@Table(name = "smt_sorting_list")
public class SmtSortingList extends ValidGroup implements Serializable {
    /**
     * 分拣单Id
     */
    @ApiModelProperty(name="sortingListId",value = "分拣单Id")
    @Excel(name = "分拣单Id", height = 20, width = 30,orderNum="1")
    @Id
    @Column(name = "sorting_list_id")
    @NotNull(groups = update.class,message = "分拣单Id不能为空")
    private Long sortingListId;

    /**
     * 分拣单号
     */
    @ApiModelProperty(name="sortingLisCode",value = "分拣单号")
    @Excel(name = "分拣单号", height = 20, width = 30,orderNum="2")
    @Column(name = "sorting_lis_code")
    @NotBlank(message = "分拣单号不能为空")
    private String sortingLisCode;

    /**
     * 工单号(预留)
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号(预留)")
    @Excel(name = "工单号(预留)", height = 20, width = 30,orderNum="3")
    @Column(name = "work_order_code")
    @NotBlank(message = "工单号不能为空")
    private String workOrderCode;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30,orderNum="4")
    @Column(name = "storage_code")
    @NotBlank(message = "储位编码不能为空")
    private String storageCode;

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

    private static final long serialVersionUID = 1L;
}