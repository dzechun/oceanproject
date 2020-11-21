package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderCardCollocationMapper extends MyMapper<SmtWorkOrderCardCollocation> {
    List<SmtWorkOrderCardCollocation> findList(SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation);
}