package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmPoProductionInfoDto;
import com.fantechs.common.base.general.entity.srm.SrmPoProductionInfo;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/17.
 */

public interface SrmPoProductionInfoService extends IService<SrmPoProductionInfo> {
    List<SrmPoProductionInfoDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<SrmPoProductionInfoDto> list);
}
