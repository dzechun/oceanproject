package com.fantechs.auth.service.impl;

import com.fantechs.auth.service.SysDefaultCustomFormDetService;
import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDetDto;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomFormDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.auth.mapper.SysDefaultCustomFormDetMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * Created by leifengzhi on 2021/09/07.
 */
@Service
public class SysDefaultCustomFormDetServiceImpl extends BaseService<SysDefaultCustomFormDet> implements SysDefaultCustomFormDetService {

    @Resource
    private SysDefaultCustomFormDetMapper sysDefaultCustomFormDetMapper;

    @Override
    public List<SysDefaultCustomFormDetDto> findList(Map<String, Object> map) {
        return sysDefaultCustomFormDetMapper.findList(map);
    }

    @Override
    public int save(SysDefaultCustomFormDet sysDefaultCustomFormDet) {
        sysDefaultCustomFormDet.setOrgId(null);
        return sysDefaultCustomFormDetMapper.insertSelective(sysDefaultCustomFormDet);
    }

    @Override
    public int update(SysDefaultCustomFormDet sysDefaultCustomFormDet) {
        sysDefaultCustomFormDet.setOrgId(null);
        return sysDefaultCustomFormDetMapper.updateByPrimaryKeySelective(sysDefaultCustomFormDet);
    }
}
