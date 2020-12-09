package com.fantechs.provider.electronic.mapper;


import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtSortingMapper extends MyMapper<SmtSorting> {

    List<SmtSortingDto> findList(Map<String, Object> map);

    int batchUpdate(List<SmtSorting> smtSortings);
}