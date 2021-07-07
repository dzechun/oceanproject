package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamNewsDto;
import com.fantechs.common.base.general.entity.eam.EamNews;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */

public interface EamNewsService extends IService<EamNews> {
    List<EamNewsDto> findList(Map<String, Object> map);
    int audit(String ids);
}
