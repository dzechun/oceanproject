package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.entity.srm.history.SrmHtPoProductionInfo;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtPoProductionInfoMapper;
import com.fantechs.provider.srm.service.SrmHtPoProductionInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/18.
 */
@Service
public class SrmHtPoProductionInfoServiceImpl extends BaseService<SrmHtPoProductionInfo> implements SrmHtPoProductionInfoService {

    @Resource
    private SrmHtPoProductionInfoMapper srmHtPoProductionInfoMapper;

    @Override
    public List<SrmHtPoProductionInfo> findList(Map<String, Object> map) {
        return srmHtPoProductionInfoMapper.findList(map);
    }

}
