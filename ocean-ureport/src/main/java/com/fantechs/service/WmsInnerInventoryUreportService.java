package com.fantechs.service;

import com.fantechs.dto.PrintModelDto;
import com.fantechs.entity.WmsInnerInventoryModel;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
public interface WmsInnerInventoryUreportService {
    List<WmsInnerInventoryModel> findList(Map<String,Object> map);

    int PrintMaterialCode(List<PrintModelDto> list);
}
