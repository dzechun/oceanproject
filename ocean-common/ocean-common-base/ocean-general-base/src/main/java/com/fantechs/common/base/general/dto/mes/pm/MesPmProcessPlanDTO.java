package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class MesPmProcessPlanDTO extends MesPmProcessPlan implements Serializable {
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
     * 组织名称
     */
    @Transient
    @ApiModelProperty(value = "组织名称",example = "组织名称")
    @Excel(name = "组织名称")
    private String organizationName;
    /**
     * 工序编码
     */
    @Transient
    @ApiModelProperty(value = "工序编码",example = "工序编码")
    @Excel(name = "工序编码")
    private String processCode;
    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(value = "工序名称",example = "工序名称")
    @Excel(name = "工序名称")
    private String processName;

    /**
     * 工序排序依据
     */
    @Transient
    @ApiModelProperty(value = "工序排序依据",example = "工序排序依据")
    private Integer orderNum;
}