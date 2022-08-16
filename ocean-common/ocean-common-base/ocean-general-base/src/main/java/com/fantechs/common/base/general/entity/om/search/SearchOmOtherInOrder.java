package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/21
 */
@Data
public class SearchOmOtherInOrder extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "otherInOrderCode",value = "其他单号")
    private String otherInOrderCode;

    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;

    @ApiModelProperty(name = "orderStatus",value = "状态")
    private String orderStatus;

    @ApiModelProperty(name = "relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;
}
