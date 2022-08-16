package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCause;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseBadnessCauseDto extends BaseBadnessCause implements Serializable {

    /**
     * 不良类别代码
     */
    @ApiModelProperty(name="badnessCategoryCode",value = "不良类别代码")
    @Excel(name = "不良类别代码", height = 20, width = 30,orderNum="4")
    @Transient
    private String badnessCategoryCode;

    /**
     * 不良类别描述
     */
    @ApiModelProperty(name="badnessCategoryDesc",value = "不良类别描述")
    @Excel(name = "不良类别描述", height = 20, width = 30,orderNum="5")
    @Transient
    private String badnessCategoryDesc;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum="8")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;
}
