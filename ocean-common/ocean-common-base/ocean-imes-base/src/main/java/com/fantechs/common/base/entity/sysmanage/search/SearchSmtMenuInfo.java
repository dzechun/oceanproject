package com.fantechs.common.base.entity.sysmanage.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchSmtMenuInfo extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 7758627630285512091L;
    /**
     * 菜单编码
     */
    @ApiModelProperty(name = "menuCode",value = "菜单编码")
    private String menuCode;

    /**
     * 菜单名称
     */
    @ApiModelProperty(name = "menuName",value = "菜单名称")
    private String menuName;
}
