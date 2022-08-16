package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/08.
 */

public interface BaseTabService extends IService<BaseTab> {

    List<BaseTabDto> findList(Map<String, Object> map);
    int insertList(List<BaseTab> baseTabs);
}
