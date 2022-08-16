package com.fantechs.common.base.general.entity.qms.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchQmsInspectionItem extends BaseQuery implements Serializable {

    /**
     * 检验项目单号
     */
    @ApiModelProperty(name = "inspectionItemCode", value = "检验项目单号")
    private String inspectionItemCode;

    /**
     * 检验项目名称
     */
    @ApiModelProperty(name = "inspectionItemName", value = "检验项目名称")
    private String inspectionItemName;

    /**
     * 检验项目水平
     */
    @ApiModelProperty(name = "inspectionItemLevel", value = "检验项目水平")
    private Long inspectionItemLevel;


    /**
     * 检验项
     */
    @ApiModelProperty(name = "inspectionNape", value = "检验项")
    private Long inspectionNape;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "queryMark", value = "查询方式标记")
    private Byte codeQueryMark;

    private static final long serialVersionUID = 1L;
}
