package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchBaseFile extends BaseQuery implements Serializable {

    /**
     * 关联ID
     */
    @ApiModelProperty(name="relevanceId" ,value="关联ID")
    private Long relevanceId;

    /**
     * 关联表名
     */
    @ApiModelProperty(name="relevanceTableName" ,value="关联表名")
    private String relevanceTableName;

}
