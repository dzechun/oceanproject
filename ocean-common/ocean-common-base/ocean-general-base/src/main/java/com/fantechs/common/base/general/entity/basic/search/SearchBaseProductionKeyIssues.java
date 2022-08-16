package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchBaseProductionKeyIssues extends BaseQuery implements Serializable {

    /**
     * 关键事项维度(1-物料 2-通用)
     */
    @ApiModelProperty(name="keyIssuesType" ,value="关键事项维度(1-物料 2-通用)")
    private Byte keyIssuesType;

    /**
     * 产品型号编码
     */
    @ApiModelProperty(name="materialCode" ,value="产品型号编码")
    private String materialCode;

    /**
     * 产品型号描述
     */
    @ApiModelProperty(name="materialDesc" ,value="产品型号描述")
    private String materialDesc;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

}
