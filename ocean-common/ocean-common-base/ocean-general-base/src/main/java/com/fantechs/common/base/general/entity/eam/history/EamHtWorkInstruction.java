package com.fantechs.common.base.general.entity.eam.history;

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
 * 作业指导书履历表（work instruction简称WI）
 * eam_ht_work_instruction
 * @author 81947
 * @date 2021-07-06 09:31:32
 */
@Data
@Table(name = "eam_ht_work_instruction")
public class EamHtWorkInstruction extends ValidGroup implements Serializable {
    /**
     * 作业指导书履历ID
     */
    @ApiModelProperty(name="htWorkInstructionId",value = "作业指导书履历ID")
    @Excel(name = "作业指导书履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_work_instruction_id")
    private Long htWorkInstructionId;

    /**
     * 作业指导书ID
     */
    @ApiModelProperty(name="workInstructionId",value = "作业指导书ID")
    @Excel(name = "作业指导书ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_instruction_id")
    private Long workInstructionId;

    /**
     * WI编码
     */
    @ApiModelProperty(name="workInstructionCode",value = "WI编码")
    @Excel(name = "WI编码", height = 20, width = 30,orderNum="") 
    @Column(name = "work_instruction_code")
    private String workInstructionCode;

    /**
     * WI名称
     */
    @ApiModelProperty(name="workInstructionName",value = "WI名称")
    @Excel(name = "WI名称", height = 20, width = 30,orderNum="") 
    @Column(name = "work_instruction_name")
    private String workInstructionName;

    /**
     * 所属工序ID
     */
    @ApiModelProperty(name="processId",value = "所属工序ID")
    @Excel(name = "所属工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_id")
    private Long processId;

    /**
     * 产品物料ID
     */
    @ApiModelProperty(name="materialId",value = "产品物料ID")
    @Excel(name = "产品物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 保密等级(1-内部 2-外部)
     */
    @ApiModelProperty(name="secrecyGrade",value = "保密等级(1-内部 2-外部)")
    @Excel(name = "保密等级(1-内部 2-外部)", height = 20, width = 30,orderNum="") 
    @Column(name = "secrecy_grade")
    private Byte secrecyGrade;

    /**
     * WI状态(1-待审核 2-通过 3-未通过)
     */
    @ApiModelProperty(name="wiStatus",value = "WI状态(1-待审核 2-通过 3-未通过)")
    @Excel(name = "WI状态(1-待审核 2-通过 3-未通过)", height = 20, width = 30,orderNum="") 
    @Column(name = "wi_status")
    private Byte wiStatus;

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

    /**
     * 制程
     */
    @ApiModelProperty(name="manufactureProcedure",value = "制程")
    @Excel(name = "制程", height = 20, width = 30,orderNum="") 
    @Column(name = "manufacture_procedure")
    private String manufactureProcedure;

    /**
     * 工艺要求及注意事项
     */
    @ApiModelProperty(name="processReqAndAnnouncements",value = "工艺要求及注意事项")
    @Excel(name = "工艺要求及注意事项", height = 20, width = 30,orderNum="") 
    @Column(name = "process_req_and_announcements")
    private String processReqAndAnnouncements;

    private static final long serialVersionUID = 1L;
}