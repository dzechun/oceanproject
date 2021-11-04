package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchEngLogisticsRecord extends BaseQuery implements Serializable {

    /**
     * 合同量单ID
     */
    @ApiModelProperty(name = "contractQtyOrderId",value = "合同量单ID")
    private Long contractQtyOrderId;

    /**
     * 标题
     */
    @ApiModelProperty(name = "title",value = "标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(name = "messageContent",value = "内容")
    private String messageContent;

    /**
     * 阅读状态(0-未读 1-已读)
     */
    @ApiModelProperty(name = "readStatus",value = "阅读状态(0-未读 1-已读)")
    private Byte readStatus;
}
