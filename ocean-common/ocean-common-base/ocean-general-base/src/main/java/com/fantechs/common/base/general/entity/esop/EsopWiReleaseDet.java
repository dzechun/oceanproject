package com.fantechs.common.base.general.entity.esop;

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
 * 作业指导书-ESOP发布管理明细
 * esop_wi_release_det
 * @author 81947
 * @date 2021-07-08 15:42:58
 */
@Data
@Table(name = "esop_wi_release_det")
public class EsopWiReleaseDet extends ValidGroup implements Serializable {
    /**
     * ESOP发布管理明细ID
     */
    @ApiModelProperty(name="wiReleaseDetId",value = "ESOP发布管理明细ID")
    @Id
    @Column(name = "wi_release_det_id")
    private Long wiReleaseDetId;

    /**
     * ESOP发布管理ID
     */
    @ApiModelProperty(name="wiReleaseId",value = "ESOP发布管理ID")
    @Column(name = "wi_release_id")
    private Long wiReleaseId;

    /**
     * ESOP发布序号
     */
    @ApiModelProperty(name="wiReleaseDetSeqNum",value = "ESOP发布序号")
    @Column(name = "wi_release_det_seq_num")
    private String wiReleaseDetSeqNum;



    /**
     * 作业指导书ID
     */
    @ApiModelProperty(name="workInstructionId",value = "作业指导书ID")
    @Column(name = "work_instruction_id")
    private Long workInstructionId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}