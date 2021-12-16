package com.fantechs.provider.guest.wanbao.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.provider.guest.wanbao.model.WanbaoReadHead;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WanbaoReadHeadDto extends WanbaoReadHead implements Serializable {

    /**
     * 月台名称
     */
    @Transient
    @ApiModelProperty(name = "platformName",value = "月台名称")
    @Excel(name = "月台名称", height = 20, width = 30,orderNum="3")
    private String platformName;

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
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
