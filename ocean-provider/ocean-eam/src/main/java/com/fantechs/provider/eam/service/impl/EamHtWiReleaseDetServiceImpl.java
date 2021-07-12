package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.entity.eam.history.EamHtWiReleaseDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamHtWiReleaseDetMapper;
import com.fantechs.provider.eam.service.EamHtWiReleaseDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */
@Service
public class EamHtWiReleaseDetServiceImpl extends BaseService<EamHtWiReleaseDet> implements EamHtWiReleaseDetService {

    @Resource
    private EamHtWiReleaseDetMapper eamHtWiReleaseDetMapper;

    @Override
    public List<EamHtWiReleaseDet> findList(Map<String, Object> map) {
      //  return eamHtWiReleaseDetMapper.findList(map);
        return null;
    }
}
