package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.PtlSortingDto;
import com.fantechs.common.base.electronic.entity.PtlSorting;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/08.
 */

public interface PtlSortingService extends IService<PtlSorting> {

    List<PtlSortingDto> findList(Map<String, Object> map);
    //批量更新
    int batchUpdate(List<PtlSorting> ptlSortings);
    int delBatchBySortingCode(List<String> sortingCodes);
    int updateStatus(String sortingCode, Byte status);
}
