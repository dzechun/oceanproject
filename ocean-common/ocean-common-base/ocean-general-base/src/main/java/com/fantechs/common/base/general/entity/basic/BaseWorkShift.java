package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 班次信息表
 * base_work_shift
 * @author 53203
 * @date 2020-12-21 09:24:12
 */
@Data
@Table(name = "base_work_shift")
public class BaseWorkShift extends ValidGroup implements Serializable {
    /**
     * 班次ID
     */
    @ApiModelProperty(name="workShiftId",value = "班次ID")
    @Id
    @Column(name = "work_shift_id")
    @NotNull(groups = update.class,message = "班次id不能为空")
    private Long workShiftId;

    /**
     * 班次编码
     */
    @ApiModelProperty(name="workShiftCode",value = "班次编码")
    @Excel(name = "班次编码", height = 20, width = 30,orderNum="1")
    @Column(name = "work_shift_code")
    @NotBlank(message = "班次编码不能为空")
    private String workShiftCode;

    /**
     * 班次名称
     */
    @ApiModelProperty(name="workShiftName",value = "班次名称")
    @Excel(name = "班次名称", height = 20, width = 30,orderNum="2")
    @Column(name = "work_shift_name")
    @NotBlank(message = "班次名称不能为空")
    private String workShiftName;

    /**
     * 班次描述
     */
    @ApiModelProperty(name="workShiftDesc",value = "班次描述")
    @Excel(name = "班次描述", height = 20, width = 30,orderNum="3")
    @Column(name = "work_shift_desc")
    private String workShiftDesc;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

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
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="6",replace = {"无效_0","有效_1"})
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
     * 班次时间集合
     */
    @ApiModelProperty(name="isDelete",value = "班次时间集合")
    @Transient
    private List<BaseWorkShiftTime> baseWorkShiftTimes;

    private static final long serialVersionUID = 1L;
}