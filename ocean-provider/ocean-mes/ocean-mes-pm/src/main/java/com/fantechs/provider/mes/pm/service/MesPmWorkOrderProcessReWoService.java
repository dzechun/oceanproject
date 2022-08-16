package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import com.fantechs.common.base.support.IService;
import com.fantechs.provider.mes.pm.vo.MesPmWorkOrderProcessReWoVo;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */

public interface MesPmWorkOrderProcessReWoService extends IService<MesPmWorkOrderProcessReWo> {
    List<MesPmWorkOrderProcessReWoDto> findList(Map<String, Object> map);
}
