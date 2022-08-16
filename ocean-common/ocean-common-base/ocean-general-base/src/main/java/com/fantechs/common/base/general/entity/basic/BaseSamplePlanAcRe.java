package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Table(name = "base_sample_plan_ac_re")
public class BaseSamplePlanAcRe extends ValidGroup implements Serializable {
    /**
     * 抽样方案AC/RE值表ID
     */
    @ApiModelProperty(name="samplePlanAcReId",value = "抽样方案AC/RE值表ID")
    @Excel(name = "抽样方案AC/RE值表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "sample_plan_ac_re_id")
    private Long samplePlanAcReId;

    /**
     * 抽样方案AQL值表ID
     */
    @ApiModelProperty(name="samplePlanAqlId",value = "抽样方案AQL值表ID")
    @Excel(name = "抽样方案AQL值表ID", height = 20, width = 30,orderNum="")
    @Column(name = "sample_plan_aql_id")
    private Long samplePlanAqlId;

    /**
     * 批量下限
     */
    @ApiModelProperty(name="batchFloor",value = "批量下限")
    @Excel(name = "批量下限", height = 20, width = 30,orderNum="")
    @Column(name = "batch_floor")
    private Integer batchFloor;

    /**
     * 批量上限
     */
    @ApiModelProperty(name="batchUpperLimit",value = "批量上限")
    @Excel(name = "批量上限", height = 20, width = 30,orderNum="")
    @Column(name = "batch_upper_limit")
    private Integer batchUpperLimit;

    /**
     * 样本数
     */
    @ApiModelProperty(name="sampleQty",value = "样本数")
    @Excel(name = "样本数", height = 20, width = 30,orderNum="")
    @Column(name = "sample_qty")
    private BigDecimal sampleQty;

    /**
     * AC值
     */
    @ApiModelProperty(name="acValue",value = "AC值")
    @Excel(name = "AC值", height = 20, width = 30,orderNum="")
    @Column(name = "ac_value")
    private Integer acValue;

    /**
     * RE值
     */
    @ApiModelProperty(name="reValue",value = "RE值")
    @Excel(name = "RE值", height = 20, width = 30,orderNum="")
    @Column(name = "re_value")
    private Integer reValue;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="")
    private Byte status;

    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long orgId;

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

    private static final long serialVersionUID = 1L;
}
