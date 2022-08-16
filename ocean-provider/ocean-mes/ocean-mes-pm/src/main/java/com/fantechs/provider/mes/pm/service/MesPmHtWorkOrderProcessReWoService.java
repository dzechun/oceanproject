package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderProcessReWo;
import com.fantechs.common.base.support.IService;
import com.fantechs.provider.mes.pm.vo.MesPmHtWorkOrderProcessReWoVo;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */

public interface MesPmHtWorkOrderProcessReWoService extends IService<MesPmHtWorkOrderProcessReWo> {
    List<MesPmHtWorkOrderProcessReWoVo> findList(Map<String, Object> map);
}
