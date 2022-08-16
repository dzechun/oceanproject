package com.fantechs.common.base.general.dto.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
@Data
public class SysCustomFormDto extends SysCustomForm implements Serializable {
    private static final long serialVersionUID = 3244097599032499131L;
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

    /**
     * 关联表单code
     */
    @Transient
    @ApiModelProperty(name = "subCode",value = "关联表单code")
    @Excel(name = "关联表单code", height = 20, width = 30,orderNum="12")
    private String subCode;

    /**
     * 关联表单名称
     */
    @Transient
    @ApiModelProperty(name = "subName",value = "关联表单名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="13")
    private String subName;

}
