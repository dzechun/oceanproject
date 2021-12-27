package com.fantechs.common.base.general.dto.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.ews.EwsWarningUserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/27
 */
@Data
public class EwsWarningUserInfoDto extends EwsWarningUserInfo implements Serializable {

    @ApiModelProperty(name = "userCode",value = "用户工号")
    @Excel(name = "用户工号", height = 20, width = 30,orderNum="1")
    private String userCode;

    @ApiModelProperty(name = "nickName",value = "用户名称")
    @Excel(name = "用户名称", height = 20, width = 30,orderNum="2")
    private String nickName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
