package com.fantechs.common.base.dto.storage;

import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2020/12/28 15:10
 * @Description: 导出仓库清单
 * @Version: 1.0
 */
@Data
public class ExportBillsDTO {
    @ExcelEntity(name="仓库清单单据")
    private WmsInStorageBillsDTO wmsInStorageBillsDTO;
    @ExcelCollection(name = "仓库清单物料详情")
    private List<WmsInStorageBillsDetDTO> wmsInStorageBillsDetDTOList;
}
