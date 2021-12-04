package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmHtCarportDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtCarport;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/23.
 */

public interface SrmHtCarportService extends IService<SrmHtCarport> {
    List<SrmHtCarportDto> findList(Map<String, Object> map);
}
