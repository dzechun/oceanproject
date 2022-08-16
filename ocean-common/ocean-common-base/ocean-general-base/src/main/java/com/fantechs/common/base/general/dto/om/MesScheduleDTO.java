package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.MesSchedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class MesScheduleDTO extends MesSchedule implements Serializable {
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
    /**
     * 产线ID
     */
    @Transient
    @ApiModelProperty(value = "产线ID",example = "产线ID")
    @Excel(name = "产线ID")
    private Long proLineId;
    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(value = "产线名称",example = "产线名称")
    @Excel(name = "产线名称")
    private String proName;
    /**
     * 产线编码
     */
    @Transient
    @ApiModelProperty(value = "产线编码",example = "产线编码")
    @Excel(name = "产线编码")
    private String proCode;
}