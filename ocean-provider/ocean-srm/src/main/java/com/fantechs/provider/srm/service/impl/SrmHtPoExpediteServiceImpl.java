package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.entity.srm.history.SrmHtPoExpedite;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtPoExpediteMapper;
import com.fantechs.provider.srm.service.SrmHtPoExpediteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/18.
 */
@Service
public class SrmHtPoExpediteServiceImpl extends BaseService<SrmHtPoExpedite> implements SrmHtPoExpediteService {

    @Resource
    private SrmHtPoExpediteMapper srmHtPoExpediteMapper;

    @Override
    public List<SrmHtPoExpedite> findList(Map<String, Object> map) {
        return srmHtPoExpediteMapper.findList(map);
    }
}
