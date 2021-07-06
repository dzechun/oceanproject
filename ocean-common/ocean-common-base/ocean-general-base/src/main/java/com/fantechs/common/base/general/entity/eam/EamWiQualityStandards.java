package com.fantechs.common.base.general.entity.eam;

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

;
;

/**
 * 作业指导书-品质标准
 * eam_wi_quality_standards
 * @author 81947
 * @date 2021-07-06 14:05:19
 */
@Data
@Table(name = "eam_wi_quality_standards")
public class EamWiQualityStandards extends ValidGroup implements Serializable {
    /**
     * 作业指导书-品质标准ID
     */
    @ApiModelProperty(name="wiQualityStandardsId",value = "作业指导书-品质标准ID")
    @Excel(name = "作业指导书-品质标准ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "wi_quality_standards_id")
    private Long wiQualityStandardsId;

    /**
     * 作业指导书ID
     */
    @ApiModelProperty(name="workInstructionId",value = "作业指导书ID")
    @Excel(name = "作业指导书ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_instruction_id")
    private Long workInstructionId;

    /**
     * 品质标准事项名称
     */
    @ApiModelProperty(name="wiQualityStandardsIssuesName",value = "品质标准事项名称")
    @Excel(name = "品质标准事项名称", height = 20, width = 30,orderNum="") 
    @Column(name = "wi_quality_standards_issues_name")
    private String wiQualityStandardsIssuesName;

    /**
     * 品质标准内容
     */
    @ApiModelProperty(name="wiQualityStandardsContent",value = "品质标准内容")
    @Excel(name = "品质标准内容", height = 20, width = 30,orderNum="") 
    @Column(name = "wi_quality_standards_content")
    private String wiQualityStandardsContent;

    /**
     * 检验方式
     */
    @ApiModelProperty(name="inspectionType",value = "检验方式")
    @Excel(name = "检验方式", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_type")
    private String inspectionType;

    /**
     * 检验频率
     */
    @ApiModelProperty(name="inspectionFrequency",value = "检验频率")
    @Excel(name = "检验频率", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_frequency")
    private String inspectionFrequency;

    /**
     * 记录
     */
    @ApiModelProperty(name="record",value = "记录")
    @Excel(name = "记录", height = 20, width = 30,orderNum="") 
    private String record;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}