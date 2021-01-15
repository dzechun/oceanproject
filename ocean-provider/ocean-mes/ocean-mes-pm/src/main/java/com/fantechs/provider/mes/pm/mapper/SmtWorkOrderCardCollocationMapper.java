package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardCollocationDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderCardCollocationMapper extends MyMapper<SmtWorkOrderCardCollocation> {
    List<SmtWorkOrderCardCollocationDto> findList(SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation);
}