package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInspectionItem extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -3896088051789821762L;
    /**
     * 父id
     */
    @ApiModelProperty(name="parentId" ,value="父id")
    private Long parentId;

    /**
     * 检验项目编码
     */
    @ApiModelProperty(name="inspectionItemCode" ,value="检验项目编码")
    private String inspectionItemCode;

    /**
     * 检验项目描述
     */
    @ApiModelProperty(name="inspectionItemDesc" ,value="检验项目描述")
    private String inspectionItemDesc;

    /**
     * 检验项目类型(1-大类 2-小类)
     */
    @ApiModelProperty(name="inspectionItemType",value = "检验项目类型(1-大类 2-小类)")
    private Byte inspectionItemType;
}
