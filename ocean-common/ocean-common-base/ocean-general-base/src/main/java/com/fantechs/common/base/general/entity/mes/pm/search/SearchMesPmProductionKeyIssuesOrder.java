package com.fantechs.common.base.general.entity.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchMesPmProductionKeyIssuesOrder extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 6392462507045784315L;
    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

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
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;
}
