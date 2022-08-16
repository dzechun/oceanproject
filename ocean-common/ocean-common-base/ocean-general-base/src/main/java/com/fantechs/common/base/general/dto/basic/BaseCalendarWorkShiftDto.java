package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseCalendarWorkShift;
import com.fantechs.common.base.general.entity.basic.BaseWorkShiftTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class BaseCalendarWorkShiftDto extends BaseCalendarWorkShift implements Serializable {

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

    /**
     * 班次时间集合
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "班次时间集合")
    private List<BaseWorkShiftTime> baseWorkShiftTimes;

    /**
     * 班次名称
     */
    @Transient
    @ApiModelProperty(name="workShiftName",value = "班次名称")
    private String workShiftName;
}
