package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author liangzhongwu
 * @create 2021-04-23 9:30
 */
@Data
public class BaseMaterialOwnerReWhDto extends BaseMaterialOwnerReWh implements Serializable {

    private static final long serialVersionUID = -6382738605980739609L;

    /**
     * 仓库编号
     */
    @Transient
    @ApiModelProperty(name="warehouseCode" ,value="仓库编号")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    /**
     * 仓库描述
     */
    @Transient
    @ApiModelProperty(name="warehouseDesc" ,value="仓库描述")
    private String warehouseDesc;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
