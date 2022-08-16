package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseOrderType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseOrderTypeDto extends BaseOrderType implements Serializable {

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="4")
    private String warehouseName;

    /**
     * 仓库编码
     */
    @Transient
    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="5")
    private String warehouseCode;

    /**
     * 货主编码
     */
    @Transient
    @ApiModelProperty(name="materialOwnerCode",value = "货主编码")
    @Excel(name = "货主编码", height = 20, width = 30,orderNum="6")
    private String materialOwnerCode;

    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="7")
    private String materialOwnerName;

    /**
     * 货主简称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerShortName",value = "货主简称")
    @Excel(name = "货主简称", height = 20, width = 30,orderNum="8")
    private String materialOwnerShortName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum="11")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;
}
