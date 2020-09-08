package com.fantechs.common.base.entity.sysmanage.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchSmtProLine  extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1363828868383986456L;

    /**
     * 线别代码
     */
    @ApiModelProperty(name="proCode" ,value="线别代码")
    private String proCode;

    /**
     * 线别名称
     */
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;

    /**
     * 线别描述
     */
    @ApiModelProperty(name="proDesc" ,value="线别描述")
    private String proDesc;

}
