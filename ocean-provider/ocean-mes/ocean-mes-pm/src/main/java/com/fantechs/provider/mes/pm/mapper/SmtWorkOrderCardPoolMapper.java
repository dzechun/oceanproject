package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.dto.apply.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardPool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderCardPoolMapper extends MyMapper<SmtWorkOrderCardPool> {
    List<SmtWorkOrderCardPoolDto> findList(SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool);
}