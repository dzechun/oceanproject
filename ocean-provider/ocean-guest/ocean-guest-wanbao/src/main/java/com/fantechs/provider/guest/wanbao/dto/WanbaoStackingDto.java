package com.fantechs.provider.guest.wanbao.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.provider.guest.wanbao.model.WanbaoStacking;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WanbaoStackingDto extends WanbaoStacking implements Serializable {

    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(name = "proName",value = "产线名称")
    @Excel(name = "产线名称", height = 20, width = 30,orderNum="4")
    private String proName;

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
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
