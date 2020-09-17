package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface SmtFactoryService {
    List<SmtFactoryDto> findList(Map<String, Object> map);
    int insert(SmtFactory smtFactory);
    int deleteById(String smtFactoryId);
    int deleteByIds(List<String> smtFactoryIds);
    int updateById(SmtFactory smtFactory);
    SmtFactory findById(String smtFactoryId);
}
