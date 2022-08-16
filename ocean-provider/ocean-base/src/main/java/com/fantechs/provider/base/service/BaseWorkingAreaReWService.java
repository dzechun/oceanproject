package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaReWDto;
import com.fantechs.common.base.general.entity.basic.BaseWorkingAreaReW;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */

public interface BaseWorkingAreaReWService extends IService<BaseWorkingAreaReW> {
    int saveDto(BaseWorkingAreaReWDto baseWorkingAreaReWDto);
    int updateDto(BaseWorkingAreaReWDto baseWorkingAreaReWDto);
    List<BaseWorkingAreaReWDto> findList(Map<String, Object> map);
}
