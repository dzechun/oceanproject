package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 抽样方案
 * @date 2020-12-23 14:51:47
 */
@Data
@Table(name = "qms_sampling_plan")
public class QmsSamplingPlan extends ValidGroup implements Serializable {
    /**
     * 抽样方案ID
     */
    @ApiModelProperty(name="samplingPlanId",value = "抽样方案ID")
    @Excel(name = "抽样方案ID", height = 20, width = 30)
    @Id
    @Column(name = "sampling_plan_id")
    private Long samplingPlanId;

    /**
     * 最小批量
     */
    @ApiModelProperty(name="minimumQuantity",value = "最小批量")
    @Excel(name = "最小批量", height = 20, width = 30)
    @Column(name = "minimum_quantity")
    private BigDecimal minimumQuantity;

    /**
     * 最大批量
     */
    @ApiModelProperty(name="maximumQuantity",value = "最大批量")
    @Excel(name = "最大批量", height = 20, width = 30)
    @Column(name = "maximum_quantity")
    private BigDecimal maximumQuantity;

    /**
     * 水准（1、I  2、II  3、III）
     */
    @ApiModelProperty(name="level",value = "水准（1、I  2、II  3、III）")
    @Excel(name = "水准（1、I  2、II  3、III）", height = 20, width = 30)
    private Byte level;

    /**
     * 样本大小
     */
    @ApiModelProperty(name="sampleSize",value = "样本大小")
    @Excel(name = "样本大小", height = 20, width = 30)
    @Column(name = "sample_size")
    private BigDecimal sampleSize;

    /**
     * AQL
     */
    @ApiModelProperty(name="aql",value = "AQL")
    @Excel(name = "AQL", height = 20, width = 30)
    private BigDecimal aql;

    /**
     * AC
     */
    @ApiModelProperty(name="ac",value = "AC")
    @Excel(name = "AC", height = 20, width = 30)
    private BigDecimal ac;

    /**
     * RE
     */
    @ApiModelProperty(name="re",value = "RE")
    @Excel(name = "RE", height = 20, width = 30)
    private BigDecimal re;

    /**
     * 检验数量（-1代表全检）
     */
    @ApiModelProperty(name="examineQuantity",value = "检验数量（-1代表全检）")
    @Excel(name = "检验数量（-1代表全检）", height = 20, width = 30)
    @Column(name = "examine_quantity")
    private BigDecimal examineQuantity;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

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

    private static final long serialVersionUID = 1L;
}
