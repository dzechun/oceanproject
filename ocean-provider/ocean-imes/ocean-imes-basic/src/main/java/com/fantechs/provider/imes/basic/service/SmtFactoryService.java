package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface SmtFactoryService  extends IService<SmtFactory>{
    List<SmtFactoryDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<SmtFactoryDto> smtFactoryDtos);
}
