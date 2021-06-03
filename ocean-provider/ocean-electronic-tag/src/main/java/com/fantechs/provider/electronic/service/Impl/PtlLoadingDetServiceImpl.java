package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.electronic.dto.PtlLoadingDetDto;
import com.fantechs.common.base.electronic.entity.PtlLoadingDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.electronic.mapper.PtlLoadingDetMapper;
import com.fantechs.provider.electronic.service.PtlLoadingDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class PtlLoadingDetServiceImpl extends BaseService<PtlLoadingDet> implements PtlLoadingDetService {

    @Resource
    private PtlLoadingDetMapper ptlLoadingDetMapper;

    @Override
    public List<PtlLoadingDetDto> findList(Map<String, Object> map) {
             return ptlLoadingDetMapper.findList(map);
    }
}
