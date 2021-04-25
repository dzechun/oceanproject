package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseBatchRules;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BaseBatchRulesDto extends BaseBatchRules implements Serializable {
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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;
    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String  warehouseName;

    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="")
    private String materialOwnerName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
