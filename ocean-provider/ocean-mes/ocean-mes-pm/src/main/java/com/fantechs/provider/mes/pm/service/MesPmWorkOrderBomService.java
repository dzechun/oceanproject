package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */

public interface MesPmWorkOrderBomService extends IService<MesPmWorkOrderBom> {
    List<MesPmWorkOrderBomDto> findList(Map<String, Object> map);
    int  batchAdd(List<MesPmWorkOrderBom> mesPmWorkOrderBoms);
}
