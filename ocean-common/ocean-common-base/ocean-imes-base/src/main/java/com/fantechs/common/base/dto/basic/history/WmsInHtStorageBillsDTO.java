package com.fantechs.common.base.dto.basic.history;

import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBills;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.EqualsAndHashCode;

@Data
public class WmsInHtStorageBillsDTO extends WmsInHtStorageBills implements Serializable {
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    @Excel(name = "创建用户名称")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称")
    private String modifiedUserName;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(value = "供应商名称",example = "供应商名称")
    @Excel(name = "供应商名称")
    private String supplierName;

    /**
     * 供应商代码
     */
    @Transient
    @ApiModelProperty(value = "供应商代码")
    @Excel(name = "供应商代码")
    private String supplierCode;

    /**
     * 收货人名称
     */
    @Transient
    @ApiModelProperty(value = "收货人名称")
    @Excel(name = "收货人名称")
    private String acceptUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;
}