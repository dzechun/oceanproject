package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseWarehousePersonnel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseWarehousePersonnelDto extends BaseWarehousePersonnel implements Serializable {

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 用户工号
     */
    @ApiModelProperty(name="userCode" ,value="用户编码")
    @Excel(name="用户编码", height = 20, width = 30)
    private String userCode;

    /**
     * 用户名称
     */
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
