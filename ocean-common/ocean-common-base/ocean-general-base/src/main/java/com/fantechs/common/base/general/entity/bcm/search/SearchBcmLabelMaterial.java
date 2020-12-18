package com.fantechs.common.base.general.entity.bcm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/12/17
 */
@Data
public class SearchBcmLabelMaterial extends BaseQuery implements Serializable {
    /**
     * 产品料号
     */
    @ApiModelProperty(name = "materialId",value = "产品料号")
    private String materialId;
    @ApiModelProperty("标签类型")
    private String labelCategoryId;
    @ApiModelProperty("标签信息")
    private String labelId;
    @ApiModelProperty("工序")
    private String processId;
}
