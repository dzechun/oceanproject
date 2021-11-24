package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.entity.srm.history.SrmHtPoExpediteRecord;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtPoExpediteRecordMapper;
import com.fantechs.provider.srm.service.SrmHtPoExpediteRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/18.
 */
@Service
public class SrmHtPoExpediteRecordServiceImpl extends BaseService<SrmHtPoExpediteRecord> implements SrmHtPoExpediteRecordService {

    @Resource
    private SrmHtPoExpediteRecordMapper srmHtPoExpediteRecordMapper;

    @Override
    public List<SrmHtPoExpediteRecord> findList(Map<String, Object> map) {
        return srmHtPoExpediteRecordMapper.findList(map);
    }
}
