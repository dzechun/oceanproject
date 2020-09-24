package com.fantechs.common.base.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by lfz on 2020/9/24.
 */
@Data
public class SmtWarehouseAreaDto extends SmtWarehouseArea implements Serializable{
    private static final long serialVersionUID = -5677327821988390035L;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="4")
    private String  warehouseName;
}
