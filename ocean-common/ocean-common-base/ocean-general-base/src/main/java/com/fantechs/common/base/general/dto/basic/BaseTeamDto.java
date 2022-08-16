package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseTeam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseTeamDto extends BaseTeam implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum = "13")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum = "15")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum = "12")
    private String organizationName;

    /**
     * 车间编码
     */
    @Excel(name = "车间编码", height = 20, width = 30,orderNum = "4")
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Excel(name = "车间名称", height = 20, width = 30,orderNum = "5")
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;

    /**
     * 车间描述
     */
    @ApiModelProperty(name = "workShopDesc",value = "车间描述")
    @Excel(name = "车间描述", height = 20, width = 30,orderNum = "6")
    private String workShopDesc;

    /**
     * 工厂ID
     */
    @Excel(name = "工厂ID",  height = 20, width = 30,orderNum = "7")
    @ApiModelProperty(name="factoryId" ,value="厂别编码")
    private Long factoryId;

    /**
     * 工厂编码
     */
    @Excel(name = "工厂编码",  height = 20, width = 30,orderNum = "8")
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    private String factoryCode;

    /**
     * 工厂名称
     */
    @Excel(name = "工厂名称", height = 20, width = 30,orderNum = "9")
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    private String factoryName;

    /**
     * 工厂描述
     */
    @Excel(name = "工厂描述", height = 20, width = 30,orderNum = "10")
    @ApiModelProperty(name="factoryDesc" ,value="厂别描述")
    private String factoryDesc;
}
