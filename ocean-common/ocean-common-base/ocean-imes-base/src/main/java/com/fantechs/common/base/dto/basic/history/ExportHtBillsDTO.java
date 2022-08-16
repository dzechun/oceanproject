package com.fantechs.common.base.dto.basic.history;

import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.fantechs.common.base.dto.basic.history.WmsInHtStorageBillsDTO;
import com.fantechs.common.base.dto.basic.history.WmsInHtStorageBillsDetDTO;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2020/12/28 15:10
 * @Description: 导出仓库清单
 * @Version: 1.0
 */
@Data
public class ExportHtBillsDTO {
    @ExcelEntity(name="仓库清单单据")
    private WmsInHtStorageBillsDTO wmsInHtStorageBillsDTO;
    @ExcelCollection(name = "仓库清单物料详情")
    private List<WmsInHtStorageBillsDetDTO> wmsInHtStorageBillsDetDTOList;
}
