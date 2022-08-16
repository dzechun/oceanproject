package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.general.entity.basic.BaseWorkingArea;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;


@Data
public class BaseWorkingAreaDto extends BaseWorkingArea implements Serializable {

    /**
     * 库区名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName",value = "库区名称")
    @Excel(name = "库区名称", height = 20, width = 30,orderNum="2")
    private String warehouseAreaName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库ID
     */
    @Transient
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="5")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;


}
