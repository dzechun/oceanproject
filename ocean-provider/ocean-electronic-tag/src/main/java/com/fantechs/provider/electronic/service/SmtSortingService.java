package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/08.
 */

public interface SmtSortingService extends IService<SmtSorting> {

    List<SmtSortingDto> findList(Map<String, Object> map);
    //批量更新
    int batchUpdate(List<SmtSorting> smtSortings);
}