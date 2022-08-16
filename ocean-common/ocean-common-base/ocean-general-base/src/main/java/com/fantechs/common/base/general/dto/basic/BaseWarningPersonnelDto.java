package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseWarningPersonnel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class BaseWarningPersonnelDto extends BaseWarningPersonnel implements Serializable {

    /**
     * 用户工号
     */
    @Column(name = "user_code")
    @ApiModelProperty(name="userCode" ,value="用户编码")
    @Excel(name="用户工号", height = 20, width = 30)
    @NotBlank(message = "用户编码不能为空")
    private String userCode;

    /**
     * 用户名称
     */
    @Column(name = "nick_name")
    @ApiModelProperty(name="nickName" ,value="用户名称")
    @Excel(name="用户名称", height = 20, width = 30)
    private String nickName;

    /**
     * 手机
     */
    @ApiModelProperty(name="mobile" ,value="手机")
    @Excel(name="手机",height = 20, width = 30)
    private String mobile;

    /**
     * 邮件地址
     */

    @ApiModelProperty(name="email" ,value="email")
    @Excel(name="邮件地址",height = 20, width = 30)
    private String email;

}
