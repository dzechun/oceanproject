package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.entity.storage.WmsInStorageBills;
import com.fantechs.common.base.entity.storage.WmsInStorageBillsDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/4 13:47
 * @Description: 同时添加清单及详情
 * @Version: 1.0
 */
@Data
public class SaveDoubleBillsDTO {
    @ApiModelProperty(value = "仓库清单信息",example = "仓库清单信息")
    private WmsInStorageBills wmsInStorageBills;
    @ApiModelProperty(value = "仓库清单详情信息集合",example = "仓库清单详情信息集合")
    private List<WmsInStorageBillsDet> wmsInStorageBillsDetList;
}
