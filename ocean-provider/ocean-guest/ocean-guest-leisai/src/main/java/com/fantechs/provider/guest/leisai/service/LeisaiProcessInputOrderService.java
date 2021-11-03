package com.fantechs.provider.guest.leisai.service;

import com.fantechs.common.base.general.dto.leisai.LeisaiProcessInputOrderDto;
import com.fantechs.common.base.general.dto.leisai.imports.LeisaiProcessInputOrderImport;
import com.fantechs.common.base.general.entity.leisai.LeisaiProcessInputOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */

public interface LeisaiProcessInputOrderService extends IService<LeisaiProcessInputOrder> {
    List<LeisaiProcessInputOrderDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<LeisaiProcessInputOrderImport> list);

    int batchSave(List<LeisaiProcessInputOrder> list);

    int batchUpdate(List<LeisaiProcessInputOrder> list);

}
