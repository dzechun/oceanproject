package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SmtHtFactoryMapper extends MyMapper<SmtHtFactory> {
    List<SmtHtFactory> findList(Map<String, Object> map);
}