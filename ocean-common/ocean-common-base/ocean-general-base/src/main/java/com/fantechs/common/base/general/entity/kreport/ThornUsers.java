package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户列表
 */
@Data
public class ThornUsers extends ValidGroup implements Serializable {

    /**
     * 用户名称
     */
    @ApiModelProperty(name="name",value = "用户名称")
    private String name;

    /**
     * unikey
     */
    @ApiModelProperty(name="unikey",value = "unikey")
    private String  unikey;

}
