package com.fantechs.provider.ews.service.impl;

import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigReWuiDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfigReWui;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.ews.mapper.EwsWarningPushConfigReWuiMapper;
import com.fantechs.provider.ews.service.EwsWarningPushConfigReWuiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/27.
 */
@Service
public class EwsWarningPushConfigReWuiServiceImpl extends BaseService<EwsWarningPushConfigReWui> implements EwsWarningPushConfigReWuiService {

    @Resource
    private EwsWarningPushConfigReWuiMapper ewsWarningPushConfigReWuiMapper;

    @Override
    public List<EwsWarningPushConfigReWuiDto> findList(Map<String, Object> map) {
        return ewsWarningPushConfigReWuiMapper.findList(map);
    }
}
