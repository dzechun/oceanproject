package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "base_ht_badness_duty")
public class BaseHtBadnessDuty extends ValidGroup implements Serializable {
    /**
     * 不良责任履历表ID
     */
    @ApiModelProperty(name="htBadnessDutyId",value = "不良责任履历表ID")
    @Excel(name = "不良责任履历表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_badness_duty_id")
    private Long htBadnessDutyId;

    /**
     * 不良责任表ID
     */
    @ApiModelProperty(name="badnessDutyId",value = "不良责任表ID")
    @Excel(name = "不良责任表ID", height = 20, width = 30,orderNum="")
    @Column(name = "badness_duty_id")
    private Long badnessDutyId;

    /**
     * 不良责任代码
     */
    @ApiModelProperty(name="badnessDutyCode",value = "不良责任代码")
    @Excel(name = "不良责任代码", height = 20, width = 30,orderNum="")
    @Column(name = "badness_duty_code")
    private String badnessDutyCode;

    /**
     * 不良责任描述
     */
    @ApiModelProperty(name="badnessDutyDesc",value = "不良责任描述")
    @Excel(name = "不良责任描述", height = 20, width = 30,orderNum="")
    @Column(name = "badness_duty_desc")
    private String badnessDutyDesc;

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

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
