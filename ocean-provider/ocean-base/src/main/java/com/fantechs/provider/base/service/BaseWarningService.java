package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseWarningDto;
import com.fantechs.common.base.general.entity.basic.BaseWarning;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/03.
 */

public interface BaseWarningService extends IService<BaseWarning> {
    List<BaseWarningDto> findList(Map<String, Object> map);
}
