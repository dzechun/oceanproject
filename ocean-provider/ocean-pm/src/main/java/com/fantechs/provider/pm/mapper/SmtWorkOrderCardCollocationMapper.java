package com.fantechs.provider.pm.mapper;

import com.fantechs.common.base.dto.apply.SmtWorkOrderCardCollocationDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderCardCollocationMapper extends MyMapper<SmtWorkOrderCardCollocation> {
    List<SmtWorkOrderCardCollocationDto> findList(SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation);
}