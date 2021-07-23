package com.fantechs.provider.baseapi.esop.service;

import com.fantechs.common.base.general.entity.restapi.esop.EsopLine;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */

public interface EsopLineService extends IService<EsopLine> {
    List<EsopLine> addLine(Map<String, Object> map);
}
