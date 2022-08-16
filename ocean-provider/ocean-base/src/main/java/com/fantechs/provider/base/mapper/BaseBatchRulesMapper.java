package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBatchRulesDto;
import com.fantechs.common.base.general.entity.basic.BaseBatchRules;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBatchRules;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseBatchRulesMapper extends MyMapper<BaseBatchRules> {
    List<BaseBatchRulesDto> findList(Map<String, Object> map);
}