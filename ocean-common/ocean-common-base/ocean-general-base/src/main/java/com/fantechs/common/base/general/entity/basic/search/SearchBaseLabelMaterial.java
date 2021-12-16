package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/12/17
 */
@Data
public class SearchBaseLabelMaterial extends BaseQuery implements Serializable {
    /**
     * 产品料号
     */
    @ApiModelProperty(name = "materialId",value = "产品料号")
    private String materialId;
    @ApiModelProperty("标签类型")
    private String labelCategoryId;
    @ApiModelProperty("标签类别编码")
    private String labelCategoryCode;
    @ApiModelProperty("标签信息")
    private String labelId;
    @ApiModelProperty("工序")
    private String processId;
    @ApiModelProperty("工序名称")
    private String processName;
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    private String materialCode;
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;
    @ApiModelProperty("产品版本")
    private String materialVersion;
    @ApiModelProperty("标签编码")
    private String labelCode;
    @ApiModelProperty("标签名称")
    private String labelName;
    @ApiModelProperty("标签版本")
    private String labelVersion;
    @ApiModelProperty("标签类别名称")
    private String labelCategoryName;
}
