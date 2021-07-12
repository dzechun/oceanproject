package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.entity.eam.history.EamHtWiRelease;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamHtWiReleaseMapper;
import com.fantechs.provider.eam.service.EamHtWiReleaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */
@Service
public class EamHtWiReleaseServiceImpl extends BaseService<EamHtWiRelease> implements EamHtWiReleaseService {

    @Resource
    private EamHtWiReleaseMapper eamHtWiReleaseMapper;

    @Override
    public List<EamHtWiRelease> findList(Map<String, Object> map) {
      //  return eamHtWiReleaseMapper.findList(map);
        return null;
    }
}
