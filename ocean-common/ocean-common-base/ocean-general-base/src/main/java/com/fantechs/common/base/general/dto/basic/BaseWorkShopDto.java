package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by lfz on 2020/9/1.
 */
@Data
public class BaseWorkShopDto extends BaseWorkShop implements Serializable {

    private static final long serialVersionUID = 6786905156525741604L;
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 工厂名称
     */
    @Transient
    @ApiModelProperty(name = "factoryName",value = "工厂名称")
    @Excel(name = "工厂名称", height = 20, width = 30,orderNum="4")
    private String factoryName;

    /**
     * 工厂编码
     */
    @Transient
    @ApiModelProperty(name = "factoryName",value = "工厂编码")
    @Excel(name = "工厂编码", height = 20, width = 30,orderNum="5")
    private String factoryCode;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
