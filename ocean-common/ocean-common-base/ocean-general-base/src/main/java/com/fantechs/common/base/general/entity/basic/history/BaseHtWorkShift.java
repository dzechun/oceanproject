package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 班次履历信息表
 * base_ht_work_shift
 * @author 53203
 * @date 2020-12-21 13:54:44
 */
@Data
@Table(name = "base_ht_work_shift")
public class BaseHtWorkShift implements Serializable {
    /**
     * 班次履历ID
     */
    @ApiModelProperty(name="htWorkShiftId",value = "班次履历ID")
    @Excel(name = "班次履历ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_work_shift_id")
    private Long htWorkShiftId;

    /**
     * 班次ID
     */
    @ApiModelProperty(name="workShiftId",value = "班次ID")
    @Excel(name = "班次ID", height = 20, width = 30)
    @Column(name = "work_shift_id")
    private Long workShiftId;

    /**
     * 班次编码
     */
    @ApiModelProperty(name="workShiftCode",value = "班次编码")
    @Excel(name = "班次编码", height = 20, width = 30)
    @Column(name = "work_shift_code")
    private String workShiftCode;

    /**
     * 班次名称
     */
    @ApiModelProperty(name="workShiftName",value = "班次名称")
    @Excel(name = "班次名称", height = 20, width = 30)
    @Column(name = "work_shift_name")
    private String workShiftName;

    /**
     * 班次描述
     */
    @ApiModelProperty(name="workShiftDesc",value = "班次描述")
    @Excel(name = "班次描述", height = 20, width = 30)
    @Column(name = "work_shift_desc")
    private String workShiftDesc;

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
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}