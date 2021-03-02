package com.fantechs.security.service.impl;

import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.security.mapper.SysCustomFormMapper;
import com.fantechs.security.service.SysCustomFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */
@Service
public class SysCustomFormServiceImpl  extends BaseService<SysCustomForm> implements SysCustomFormService {

         @Resource
         private SysCustomFormMapper sysCustomFormMapper;

    @Override
    public List<SysCustomFormDto> findList(Map<String, Object> map) {
        return sysCustomFormMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysCustomForm sysCustomForm) {
        return sysCustomFormMapper.updateByPrimaryKey(sysCustomForm);
    }
}
