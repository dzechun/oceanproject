package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchSmtFactory  extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 5247071019362570766L;
    /**
     * 工厂编码
     */
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    private String factoryCode;

    /**
     * 工厂名称
     */
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    private String factoryName;

    /**
     * 工厂描述
     */
    @ApiModelProperty(name="factoryDesc" ,value="厂别描述")
    private String factoryDesc;

}
