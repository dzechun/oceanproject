package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmPoExpediteDto;
import com.fantechs.common.base.general.entity.srm.SrmPoExpedite;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/18.
 */

public interface SrmPoExpediteService extends IService<SrmPoExpedite> {
    List<SrmPoExpediteDto> findList(Map<String, Object> map);
}
