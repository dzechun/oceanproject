package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 拆批作业明细
 * mes_pm_break_bulk_det
 * @author mr.lei
 * @date 2021-01-18 14:21:26
 */
@Data
@Table(name = "mes_pm_break_bulk_det")
public class MesPmBreakBulkDet extends ValidGroup implements Serializable {
    /**
     * 拆批作业明细id
     */
    @ApiModelProperty(name="breakBulkDetId",value = "拆批作业明细id")
    @Excel(name = "拆批作业明细id", height = 20, width = 30)
    @Id
    @Column(name = "break_bulk_det_id")
    private Long breakBulkDetId;

    /**
     * 拆批作业id
     */
    @ApiModelProperty(name="breakBulkId",value = "拆批作业id")
    @Excel(name = "拆批作业id", height = 20, width = 30)
    @Column(name = "break_bulk_id")
    private Long breakBulkId;

    /**
     * 子批次号
     */
    @ApiModelProperty(name="childLotNo",value = "子批次号")
    @Excel(name = "子批次号", height = 20, width = 30)
    @Column(name = "child_lot_no")
    private String childLotNo;

    /**
     * 拆分数量
     */
    @ApiModelProperty(name="breakBulkQty",value = "拆分数量")
    @Excel(name = "拆分数量", height = 20, width = 30)
    @Column(name = "break_bulk_qty")
    private BigDecimal breakBulkQty;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30)
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30)
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30)
    private String option3;

    private static final long serialVersionUID = 1L;
}