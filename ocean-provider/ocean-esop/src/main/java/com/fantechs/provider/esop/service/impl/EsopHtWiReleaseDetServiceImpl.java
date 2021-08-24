package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.general.dto.esop.EsopHtWiReleaseDetDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiReleaseDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.esop.mapper.EsopHtWiReleaseDetMapper;
import com.fantechs.provider.esop.service.EsopHtWiReleaseDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */
@Service
public class EsopHtWiReleaseDetServiceImpl extends BaseService<EsopHtWiReleaseDet> implements EsopHtWiReleaseDetService {

    @Resource
    private EsopHtWiReleaseDetMapper esopHtWiReleaseDetMapper;

    @Override
    public List<EsopHtWiReleaseDetDto> findList(Map<String, Object> map) {
        return esopHtWiReleaseDetMapper.findHtList(map);
    }
}
