package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtTeam;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/15.
 */

public interface BaseHtTeamService extends IService<BaseHtTeam> {
    List<BaseHtTeam> findHtList(Map<String, Object> map);
}
