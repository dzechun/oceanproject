package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDetDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCartonDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/05/08.
 */

public interface MesSfcProductCartonDetService extends IService<MesSfcProductCartonDet> {
    List<MesSfcProductCartonDetDto> findList(Map<String, Object> map);

    /**
     * 关联查询
     * @param map
     * @return
     */
    List<MesSfcProductCartonDetDto> findRelationList(Map<String, Object> map);
}
