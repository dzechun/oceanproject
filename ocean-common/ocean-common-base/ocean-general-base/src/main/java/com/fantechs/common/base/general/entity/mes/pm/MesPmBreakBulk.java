package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.slf4j.Logger;

/**
 * 拆批作业
 * mes_pm_break_bulk
 * @author mr.lei
 * @date 2021-01-18 11:30:42
 */
@Data
@Table(name = "mes_pm_break_bulk")
public class MesPmBreakBulk extends ValidGroup implements Serializable {
    /**
     * 拆批作业id
     */
    @ApiModelProperty(name="breakBulkId",value = "拆批作业id")
    @Excel(name = "拆批作业id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "break_bulk_id")
    private Long breakBulkId;

    /**
     * 拆批作业单号
     */
    @ApiModelProperty(name="breakBulkCode",value = "拆批作业单号")
    @Excel(name = "拆批作业单号", height = 20, width = 30,orderNum="") 
    @Column(name = "break_bulk_code")
    private String breakBulkCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 批次号
     */
    @ApiModelProperty(name = "batchNo",value = "批次号")
    @Column(name = "batch_no")
    private String batchNo;
    /**
     * 拆批数
     */
    @ApiModelProperty(name = "breakBulkBatchQty",value = "拆批数")
    @Column(name = "break_bulk_batch_qty")
    private BigDecimal breakBulkBatchQty;

    /**
     * 工序id
     */
    @Transient
    @ApiModelProperty(name = "processId",value = "工序id")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

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
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="")
    private String option3;

    /**
     * 作业类型
     */
    @ApiModelProperty(name = "breakBulkType",value = "作业类型(1、拆批作业，2、合批作业)")
    @Column(name = "break_bulk_type")
    private Byte breakBulkType;

    private List<MesPmBreakBulkDet> mesPmBreakBulkDets;

    private static final long serialVersionUID = 1L;
}