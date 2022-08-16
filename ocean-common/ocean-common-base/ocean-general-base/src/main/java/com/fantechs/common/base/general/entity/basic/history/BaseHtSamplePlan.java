package com.fantechs.common.base.general.entity.basic.history;

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
import java.util.Date;


@Data
@Table(name = "base_ht_sample_plan")
public class BaseHtSamplePlan extends ValidGroup implements Serializable {
    /**
     * 抽样方案履历表ID
     */
    @ApiModelProperty(name="htSamplePlanId",value = "抽样方案履历表ID")
    @Excel(name = "抽样方案履历表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_sample_plan_id")
    private Long htSamplePlanId;

    /**
     * 抽样方案ID
     */
    @ApiModelProperty(name="samplePlanId",value = "抽样方案ID")
    @Excel(name = "抽样方案ID", height = 20, width = 30,orderNum="")
    @Column(name = "sample_plan_id")
    private Long samplePlanId;

    /**
     * 抽样方案编码
     */
    @ApiModelProperty(name="samplePlanCode",value = "抽样方案编码")
    @Excel(name = "抽样方案编码", height = 20, width = 30,orderNum="")
    @Column(name = "sample_plan_code")
    private String samplePlanCode;

    /**
     * 抽样方案名称
     */
    @ApiModelProperty(name="samplePlanDesc",value = "抽样方案名称")
    @Excel(name = "抽样方案名称", height = 20, width = 30,orderNum="")
    @Column(name = "sample_plan_desc")
    private String samplePlanDesc;

    /**
     * 检验水平
     */
    @ApiModelProperty(name="testLevel",value = "检验水平")
    @Excel(name = "检验水平", height = 20, width = 30,orderNum="")
    @Column(name = "test_level")
    private String testLevel;

    /**
     * 检验标准类型
     */
    @ApiModelProperty(name="sampleStandardId",value = "检验标准类型")
    @Excel(name = "检验标准类型", height = 20, width = 30,orderNum="")
    @Column(name = "sample_standard_id")
    private Long sampleStandardId;

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
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}
