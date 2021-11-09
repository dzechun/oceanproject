package com.fantechs.common.base.general.dto.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

@Data
public class CallAgvAgvTaskDto extends CallAgvAgvTask {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 起始库区
     */
    @Transient
    @ApiModelProperty(name = "startTaskPointName",value = "起始库区")
    @Excel(name = "起始库区", height = 20, width = 30,orderNum="3")
    private String startTaskPointName;

    /**
     * 目标库区
     */
    @Transient
    @ApiModelProperty(name = "endTaskPointName",value = "目标库区")
    @Excel(name = "目标库区", height = 20, width = 30,orderNum="4")
    private String endTaskPointName;

    /**
     * 货架
     */
    @Transient
    @ApiModelProperty(name = "vehicleName",value = "货架")
    @Excel(name = "货架", height = 20, width = 30,orderNum="5")
    private String vehicleName;
}
