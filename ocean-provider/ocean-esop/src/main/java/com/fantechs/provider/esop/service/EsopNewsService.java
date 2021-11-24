package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopNewsDto;
import com.fantechs.common.base.general.entity.esop.EsopNews;
import com.fantechs.common.base.support.IService;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */

public interface EsopNewsService extends IService<EsopNews> {
    List<EsopNewsDto> findList(Map<String, Object> map);
    int audit(String ids) throws UnknownHostException;
    EsopNews selectByKey(Long key);
}
