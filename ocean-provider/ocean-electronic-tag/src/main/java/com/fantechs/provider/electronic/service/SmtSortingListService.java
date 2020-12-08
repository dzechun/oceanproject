package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.SmtSortingList;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/08.
 */

public interface SmtSortingListService extends IService<SmtSortingList> {

    List<SmtSortingList> findList(Map<String, Object> map);
}
