package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.entity.srm.history.SrmHtCarport;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtCarportMapper;
import com.fantechs.provider.srm.service.SrmHtCarportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/23.
 */
@Service
public class SrmHtCarportServiceImpl extends BaseService<SrmHtCarport> implements SrmHtCarportService {

    @Resource
    private SrmHtCarportMapper srmHtCarportMapper;

    @Override
    public List<SrmHtCarport> findList(Map<String, Object> map) {
        return srmHtCarportMapper.findList(map);
    }

}
