package com.fantechs.common.base.dto.apply.history;

import com.fantechs.common.base.entity.apply.history.MesHtSchedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class MesHtScheduleDTO extends MesHtSchedule implements Serializable {
    /**
    * 创建用户名称
    */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    @Excel(name = "创建用户名称")
    private String createUserName;
    /**
    * 修改用户名称
    */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称")
    private String modifiedUserName;
}