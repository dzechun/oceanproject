package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.entity.storage.WmsStorageBillsDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2020/12/21 17:35
 * @Description: 保存单据清单物料详情
 * @Version: 1.0
 */
@Data
public class SaveBilssDet {
    @ApiModelProperty(value = "单据清单ID",example = "单据清单ID")
    private Long storageBillsId;
    @ApiModelProperty(value = "单据清单ID",example = "单据清单ID")
    private List<WmsStorageBillsDet> wmsStorageBillsDetList;
}
