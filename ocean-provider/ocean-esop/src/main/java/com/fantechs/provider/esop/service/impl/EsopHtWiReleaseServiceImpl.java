package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.general.dto.esop.EsopHtWiReleaseDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiRelease;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.esop.mapper.EsopHtWiReleaseMapper;
import com.fantechs.provider.esop.service.EsopHtWiReleaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */
@Service
public class EsopHtWiReleaseServiceImpl extends BaseService<EsopHtWiRelease> implements EsopHtWiReleaseService {

    @Resource
    private EsopHtWiReleaseMapper esopHtWiReleaseMapper;

    @Override
    public List<EsopHtWiReleaseDto> findHtList(Map<String, Object> map) {
        return esopHtWiReleaseMapper.findHtList(map);
    }
}
