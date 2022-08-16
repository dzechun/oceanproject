package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseCalendar;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class BaseCalendarDto extends BaseCalendar implements Serializable {

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
     * 线别代码
     */
    @Transient
    @ApiModelProperty(name="proCode" ,value="线别代码")
    @Excel(name = "线别代码", height = 20, width = 30)
    private String proCode;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "线别名称", height = 20, width = 30)
    private String proName;

    /**
     * 线别描述
     */
    @Transient
    @ApiModelProperty(name="proDesc" ,value="线别描述")
    @Excel(name = "线别描述", height = 20, width = 30)
    private String proDesc;


    /**
     * 班次编码
     */
    @Transient
    @ApiModelProperty(name="workShiftCode",value = "班次编码")
    @Excel(name = "班次编码", height = 20, width = 30,orderNum="")
    @Column(name = "work_shift_code")
    private String workShiftCode;

    /**
     * 班次Id
     */
    @Transient
    @ApiModelProperty(name="workShiftId",value = "班次Id")
    @Excel(name = "班次Id", height = 20, width = 30,orderNum="")
    private Long workShiftId;

    /**
     * 班次名称
     */
    @Transient
    @ApiModelProperty(name="workShiftName",value = "班次名称")
    @Excel(name = "班次名称", height = 20, width = 30,orderNum="")
    private String workShiftName;

    /**
     * 班次描述
     */
    @Transient
    @ApiModelProperty(name="workShiftDesc",value = "班次描述")
    @Excel(name = "班次描述", height = 20, width = 30,orderNum="")
    private String workShiftDesc;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 日历班次关系集合
     */
    @ApiModelProperty(name="baseCalendarWorkShiftDtos",value = "日历班次关系集合")
    @Excel(name = "日历班次关系集合", height = 20, width = 30,orderNum="")
    @Column(name = "baseCalendarWorkShiftDtos")
    private List<BaseCalendarWorkShiftDto> baseCalendarWorkShiftDtos=new LinkedList<>();
}
