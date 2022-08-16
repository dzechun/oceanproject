package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2020/9/25
 */
@ApiModel
@Data
public class SearchBaseWorkshopSection extends BaseQuery implements Serializable {
    private static final long serialVersionUID = 9081822012477093927L;
    /**
     * 工段代码
     */
    @ApiModelProperty("工段代码")
    private String sectionCode;
    /**
     * 工段名称
     */
    @ApiModelProperty("工段名称")
    private String sectionName;
    /**
     * 工段描述
     */
    @ApiModelProperty("工段描述")
    private String sectionDesc;
}
