package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.entity.basic.history.BaseHtTeam;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtTeamMapper;
import com.fantechs.provider.base.service.BaseHtTeamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@Service
public class BaseHtTeamServiceImpl  extends BaseService<BaseHtTeam> implements BaseHtTeamService {

    @Resource
    private BaseHtTeamMapper baseHtTeamMapper;

    @Override
    public List<BaseHtTeam> findHtList(Map<String, Object> map) {
        return baseHtTeamMapper.findHtList(map);
    }
}
