package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.entity.storage.WmsInStorageBillsDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2020/12/21 17:35
 * @Description: 保存单据清单物料详情
 * @Version: 1.0
 */
@Data
public class SaveBillsDetDTO {
    @ApiModelProperty(value = "单据清单ID",example = "单据清单ID")
    @NotNull(message = "单据清单ID不能为空")
    private Long storageBillsId;
    @ApiModelProperty(value = "单据清单详情信息",example = "单据清单详情信息")
    @NotNull(message = "单据清单详情信息不能为空")
    private List<WmsInStorageBillsDet> wmsStorageBillsDetList;
}
