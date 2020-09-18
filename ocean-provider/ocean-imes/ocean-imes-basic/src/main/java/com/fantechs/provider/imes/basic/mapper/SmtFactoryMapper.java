package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SmtFactoryMapper extends MyMapper<SmtFactory> {
    List<SmtFactoryDto> findList(Map<String, Object> map);
    int delBatch(List<Long> list);
}