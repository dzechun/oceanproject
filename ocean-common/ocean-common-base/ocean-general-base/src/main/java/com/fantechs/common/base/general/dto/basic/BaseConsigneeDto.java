package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseAddress;
import com.fantechs.common.base.general.entity.basic.BaseConsignee;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseConsigneeDto extends BaseConsignee implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name = "materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30)
    private String materialOwnerName;

}
