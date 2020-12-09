package com.fantechs.provider.electronic.mapper;


import com.fantechs.common.base.electronic.dto.SmtSortingListDto;
import com.fantechs.common.base.electronic.entity.SmtSortingList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtSortingListMapper extends MyMapper<SmtSortingList> {

    List<SmtSortingListDto> findList(Map<String, Object> map);
}