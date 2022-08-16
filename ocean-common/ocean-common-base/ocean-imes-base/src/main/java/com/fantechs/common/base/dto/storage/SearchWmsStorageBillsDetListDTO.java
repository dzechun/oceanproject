package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 仓库清单详情表搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchWmsStorageBillsDetListDTO extends BaseQuery implements Serializable  {
    @ApiModelProperty(value = "仓库清单ID")
    @NotNull(message = "仓库清单ID不能为空")
    private Long storageBillsId;
}
