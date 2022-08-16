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
import java.util.Date;
import java.util.List;


@Data
@Table(name = "base_sample_plan")
public class BaseSamplePlan extends ValidGroup implements Serializable {
    /**
     * 抽样方案ID
     */
    @ApiModelProperty(name="samplePlanId",value = "抽样方案ID")
    @Id
    @Column(name = "sample_plan_id")
    private Long samplePlanId;

    /**
     * 抽样方案编码
     */
    @ApiModelProperty(name="samplePlanCode",value = "抽样方案编码")
    @Excel(name = "抽样方案编码", height = 20, width = 30,orderNum="1")
    @Column(name = "sample_plan_code")
    private String samplePlanCode;

    /**
     * 抽样方案名称
     */
    @ApiModelProperty(name="samplePlanDesc",value = "抽样方案名称")
    @Excel(name = "抽样方案名称", height = 20, width = 30,orderNum="2")
    @Column(name = "sample_plan_desc")
    private String samplePlanDesc;

    /**
     * 检验水平
     */
    @ApiModelProperty(name="testLevel",value = "检验水平")
    @Excel(name = "检验水平", height = 20, width = 30,orderNum="3")
    @Column(name = "test_level")
    private String testLevel;

    /**
     * 检验标准类型
     */
    @ApiModelProperty(name="sampleStandardId",value = "检验标准类型")
    @Column(name = "sample_standard_id")
    private Long sampleStandardId;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="status",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * AQL集合
     */
    @ApiModelProperty(name="list",value = "AQL集合")
    private List<BaseSamplePlanAql> list;

    private static final long serialVersionUID = 1L;
}
