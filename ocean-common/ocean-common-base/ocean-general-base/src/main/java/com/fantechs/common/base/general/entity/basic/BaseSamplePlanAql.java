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
import java.util.List;


@Data
@Table(name = "base_sample_plan_aql")
public class BaseSamplePlanAql extends ValidGroup implements Serializable {
    /**
     * 抽样方案AQL值表ID
     */
    @ApiModelProperty(name="samplePlanAqlId",value = "抽样方案AQL值表ID")
    @Excel(name = "抽样方案AQL值表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "sample_plan_aql_id")
    private Long samplePlanAqlId;

    /**
     * 检验严格度(1-放宽 2-正常 3-加严)
     */
    @ApiModelProperty(name="inspectionRigorLevel",value = "检验严格度(1-放宽 2-正常 3-加严)")
    @Excel(name = "检验严格度(1-放宽 2-正常 3-加严)", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_rigor_level")
    private Byte inspectionRigorLevel;

    /**
     * AQL值
     */
    @ApiModelProperty(name="aqlValue",value = "AQL值")
    @Excel(name = "AQL值", height = 20, width = 30,orderNum="")
    @Column(name = "aql_value")
    private BigDecimal aqlValue;

    /**
     * 抽样方案ID
     */
    @ApiModelProperty(name="samplePlanId",value = "抽样方案ID")
    @Excel(name = "抽样方案ID", height = 20, width = 30,orderNum="")
    @Column(name = "sample_plan_id")
    private Long samplePlanId;

    /**
     * 抽样方式(1-一次抽样 2-二次抽样 3-多重抽样 4-序贯抽样)
     */
    @ApiModelProperty(name="sampleType",value = "抽样方式(1-一次抽样 2-二次抽样 3-多重抽样 4-序贯抽样)")
    @Excel(name = "抽样方式(1-一次抽样 2-二次抽样 3-多重抽样 4-序贯抽样)", height = 20, width = 30,orderNum="")
    @Column(name = "sample_type")
    private Byte sampleType;

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

    /**
     * AcRe集合
     */
    @ApiModelProperty(name="list",value = "AcRe集合")
    private List<BaseSamplePlanAcRe> list;

    private static final long serialVersionUID = 1L;
}
