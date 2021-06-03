package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.entity.PtlLoading;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */

public interface PtlLoadingService extends IService<PtlLoading> {

    List<PtlLoading> findList(Map<String, Object> map);

    List<PtlLoading> findHtList(Map<String, Object> map);

}
