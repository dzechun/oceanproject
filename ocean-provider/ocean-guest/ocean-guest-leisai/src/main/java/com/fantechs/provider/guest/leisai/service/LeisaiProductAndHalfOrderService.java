package com.fantechs.provider.guest.leisai.service;

import com.fantechs.common.base.general.dto.leisai.LeisaiProductAndHalfOrderDto;
import com.fantechs.common.base.general.dto.leisai.imports.LeisaiProductAndHalfOrderImport;
import com.fantechs.common.base.general.entity.leisai.LeisaiProductAndHalfOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/27.
 */

public interface LeisaiProductAndHalfOrderService extends IService<LeisaiProductAndHalfOrder> {
    List<LeisaiProductAndHalfOrderDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<LeisaiProductAndHalfOrderImport> leisaiProductAndHalfOrderImports);
}
