package com.fantechs.common.base.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchSysImportTemplate extends BaseQuery implements Serializable {
    /**
     * 模板编码
     */
    @ApiModelProperty(name = "importTemplateCode",value = "模板编码")
    private String importTemplateCode;

    /**
     * 模板名称
     */
    @ApiModelProperty(name = "importTemplateName",value = "模板名称")
    private String importTemplateName;

    /**
     * 模板所属菜单id
     */
    @ApiModelProperty(name = "menuId",value = "模板所属菜单id")
    private Long menuId;

    /**
     * 模板所属菜单id列表
     */
    /*@ApiModelProperty(name = "menuIds",value = "模板所属菜单id列表")
    private List<Long> menuIds;*/
}
