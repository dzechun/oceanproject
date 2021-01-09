package com.fantechs.security.service.impl;

import com.fantechs.common.base.general.dto.security.SysCustomFormDetDto;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.security.mapper.SysCustomFormDetMapper;
import com.fantechs.security.service.SysCustomFormDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */
@Service
public class SysCustomFormDetServiceImpl  extends BaseService<SysCustomFormDet> implements SysCustomFormDetService {

         @Resource
         private SysCustomFormDetMapper sysCustomFormDetMapper;

    @Override
    public List<SysCustomFormDetDto> findList(Map<String, Object> map) {
        return sysCustomFormDetMapper.findList(map);
    }
}
