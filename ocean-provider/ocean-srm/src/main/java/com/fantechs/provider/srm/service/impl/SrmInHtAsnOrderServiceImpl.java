package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmInHtAsnOrderMapper;
import com.fantechs.provider.srm.service.SrmInHtAsnOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class SrmInHtAsnOrderServiceImpl extends BaseService<SrmInHtAsnOrder> implements SrmInHtAsnOrderService {

    @Resource
    private SrmInHtAsnOrderMapper srmInHtAsnOrderMapper;

    @Override
    public List<SrmInHtAsnOrder> findList(Map<String, Object> map) {
        return srmInHtAsnOrderMapper.findList(map);
    }

}
