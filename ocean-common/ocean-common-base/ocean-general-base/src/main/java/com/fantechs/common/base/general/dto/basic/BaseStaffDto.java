package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseStaff;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class BaseStaffDto extends BaseStaff implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 班组代码
     */
    @Transient
    @ApiModelProperty(name="teamCode",value = "班组代码")
    @Excel(name = "班组代码", height = 20, width = 30)
    private String teamCode;

    /**
     * 班组名称
     */
    @Transient
    @ApiModelProperty(name="teamName",value = "班组名称")
    @Excel(name = "班组名称", height = 20, width = 30)
    private String teamName;

    /**
     * 班组描述
     */
    @Transient
    @ApiModelProperty(name="teamDesc",value = "班组描述")
    private String teamDesc;

    /**
     * 车间编码
     */
    @Transient
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Transient
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    @Excel(name = "车间名称", height = 20, width = 30)
    private String workShopName;

    /**
     * 车间描述
     */
    @Transient
    @ApiModelProperty(name = "workShopDesc",value = "车间描述")
    private String workShopDesc;

    /**
     * 工厂名称
     */
    @Transient
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    @Excel(name = "工厂名称", height = 20, width = 30)
    private String factoryName;

    /**
     * 工厂编码
     */
    @Transient
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    @Excel(name = "工厂编码", height = 20, width = 30)
    private String factoryCode;

    /**
     * 工厂描述
     */
    @Transient
    @ApiModelProperty(name="factoryDesc" ,value="厂别描述")
    private String factoryDesc;

}
