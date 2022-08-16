package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInAndOutRuleType extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -3896088051789821762L;
    /**
     * 出入库规则类型编码
     */
    @ApiModelProperty(name="inAndOutRuleTypeCode" ,value="出入库规则类型编码")
    private String inAndOutRuleTypeCode;

    /**
     * 出入库规则类型名称
     */
    @ApiModelProperty(name="inAndOutRuleTypeName" ,value="出入库规则类型名称")
    private String inAndOutRuleTypeName;

    /**
     * 类型(1-入库 2-出库)
     */
    @ApiModelProperty(name="category" ,value="类型(1-入库 2-出库)")
    private Byte category;

}
