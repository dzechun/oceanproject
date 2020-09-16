package com.fantechs.common.base.dot.sysmanage;

import cn.afterturn.easypoi.excel.annotation.Excel;

import com.fantechs.common.base.entity.sysmanage.SmtFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by lfz on 2020/9/1.
 */
@Data
public class SmtFactoryDto extends SmtFactory implements Serializable {

    private static final long serialVersionUID = 6786905156525741604L;
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

}
