package com.fantechs.common.base.general.entity.qms.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

;

/**
 * 检验类型表
 * qms_inspection_type
 *
 * @author jbb
 * @date 2020-12-23 11:09:14
 */
@Data
public class SearchQmsInspectionType extends BaseQuery implements Serializable {

    /**
     * 检验类型单号
     */
    @ApiModelProperty(name = "inspectionTypeCode", value = "检验类型单号")
    private String inspectionTypeCode;

    /**
     * 检验类型名称
     */
    @ApiModelProperty(name = "inspectionTypeName", value = "检验类型名称")
    private String inspectionTypeName;

    /**
     * 检验类型水平
     */
    @ApiModelProperty(name = "inspectionTypeLevel", value = "检验类型水平")
    private Long inspectionTypeLevel;

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
