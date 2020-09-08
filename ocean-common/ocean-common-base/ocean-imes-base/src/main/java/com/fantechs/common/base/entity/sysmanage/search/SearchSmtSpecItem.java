package com.fantechs.common.base.entity.sysmanage.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/8/19 14:00
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchSmtSpecItem extends BaseQuery implements Serializable{
    private static final long serialVersionUID = 4690983492972353900L;
    /**
     * 配置项代码
     */
    @ApiModelProperty(name="specCode" ,value="配置项代码")
    private String specCode;

    /**
     * 配置项名称
     */
    @ApiModelProperty(name="specName" ,value="配置项名称")
    private String specName;

    /**
     * 参数
     */
    @ApiModelProperty(name="para" ,value="参数")
    private String para;
}
