package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class BaseWorkerDto extends BaseWorker implements Serializable {
    /**
     * 用户帐号
     */
    @ApiModelProperty(name = "userName", value = "用户帐号")
    @Excel(name = "用户帐号", height = 20, width = 30, orderNum = "0")
    private String userName;
    /**
     * 用户名称
     */
    @ApiModelProperty(name = "nickName", value = "用户名称")
    @Excel(name = "用户名称", height = 20, width = 30, orderNum = "1")
    private String nickName;
    /**
     * 工号
     */
    @ApiModelProperty(name="workerCode",value = "用户工号")
    @Excel(name = "用户工号", height = 20, width = 30, orderNum = "2")
    private String userCode;
    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30, orderNum = "3")
    private String warehouseName;
    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30, orderNum = "6")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30, orderNum = "8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName",value = "组织名称")
//    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 工作区工作人员关系
     */
    @ApiModelProperty(name = "baseWorkingAreaReWDtoList", value = "工作区工作人员关系")
    private List<BaseWorkingAreaReWDto> baseWorkingAreaReWDtoList;
}
