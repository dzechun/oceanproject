package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseSampleStandardDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleStandard;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/06.
 */

public interface BaseSampleStandardService extends IService<BaseSampleStandard> {
    List<BaseSampleStandardDto> findList(Map<String, Object> map);
}
