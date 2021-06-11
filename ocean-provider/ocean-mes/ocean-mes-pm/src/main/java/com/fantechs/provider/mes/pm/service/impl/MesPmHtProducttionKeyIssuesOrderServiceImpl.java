package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProducttionKeyIssuesOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.MesPmHtProducttionKeyIssuesOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmHtProducttionKeyIssuesOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */
@Service
public class MesPmHtProducttionKeyIssuesOrderServiceImpl extends BaseService<MesPmHtProducttionKeyIssuesOrder> implements MesPmHtProducttionKeyIssuesOrderService {

    @Resource
    private MesPmHtProducttionKeyIssuesOrderMapper mesPmHtProducttionKeyIssuesOrderMapper;

    @Override
    public List<MesPmHtProducttionKeyIssuesOrder> findHtList(Map<String, Object> map) {
        return mesPmHtProducttionKeyIssuesOrderMapper.findHtList(map);
    }
}
