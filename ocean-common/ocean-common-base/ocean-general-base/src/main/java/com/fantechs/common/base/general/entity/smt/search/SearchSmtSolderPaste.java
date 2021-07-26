package com.fantechs.common.base.general.entity.smt.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/7/22
 */
@Data
public class SearchSmtSolderPaste extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    @ApiModelProperty(name = "solderPasteStatus",value = "锡膏状态(1-入冰库 2-回温 3-搅拌 4-开封 5-上料 6-用完 7-回冰 8-报废)")
    private Byte solderPasteStatus;
}
