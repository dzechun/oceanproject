package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SearchBaseBatchRules extends BaseQuery implements Serializable {
    /**
     * 仓库名称
     */
    @ApiModelProperty("仓库名称")
    private String warehouseName;

    /**
     * 货主名称
     */
    @ApiModelProperty("货主名称")
    private String materialOwnerName;

    /**
     * 批次规则名称
     */
    @ApiModelProperty("批次规则名称")
    private String batchRulesName;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Byte status;

    /**
     * 创建名称
     */
    @ApiModelProperty("创建名称")
    private String createUserName;
}
