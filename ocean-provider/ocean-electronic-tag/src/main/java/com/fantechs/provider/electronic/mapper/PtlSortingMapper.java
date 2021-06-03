package com.fantechs.provider.electronic.mapper;


import com.fantechs.common.base.electronic.dto.PtlSortingDto;
import com.fantechs.common.base.electronic.entity.PtlSorting;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PtlSortingMapper extends MyMapper<PtlSorting> {

    List<PtlSortingDto> findList(Map<String, Object> map);

    int batchUpdate(List<PtlSorting> ptlSortings);
    int delBatchBySortingCode(List<String> sortingCodes);
}